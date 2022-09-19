package com.heima.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heima.article.entity.ApArticle;
import com.heima.article.entity.ApArticleContent;
import com.heima.article.entity.ApAuthor;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.article.service.ApArticleContentService;
import com.heima.article.service.ApArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.article.service.ApAuthorService;
import com.heima.article.service.ArticleFreemarkerService;
import com.heima.common.enums.AppHttpCodeEnum;
import com.heima.common.exception.LeadException;
import com.heima.common.threadpool.ThreadPoolConfig;
import com.heima.common.util.BeanHelper;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.media.dtos.WmNewsResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.*;

/**
 * <p>
 * 文章信息表，存储已发布的文章 服务实现类
 * </p>
 *
 * @author HM
 * @since 2022-09-04
 */
@Service
@Slf4j
public class ApArticleServiceImpl extends ServiceImpl<ApArticleMapper, ApArticle> implements ApArticleService {

    @Autowired
    private ApAuthorService apAuthorService;
    @Autowired
    private ApArticleContentService apArticleContentService;
    @Autowired
    private ArticleFreemarkerService articleFreemarkerService;
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Override
    public void upOrDown(Map<String, Long> map) {
        Long articleId = map.get("articleId");
        Long enable = map.get("enable");
        LambdaUpdateWrapper<ApArticle> updateWrapper = new LambdaUpdateWrapper<>();
        //注意enable的格式
        updateWrapper.eq(ApArticle::getId, articleId).set(ApArticle::getIsDown, enable.intValue() == 0);
        update(updateWrapper);

    }

    /**
     * @param dto
     * @return
     * @description : 保存文章
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveArticle(WmNewsResultDTO dto) {
        //自媒体文章id
        Integer wmNewsId = dto.getId();
        LambdaQueryWrapper<ApArticle> queryWrapper = new LambdaQueryWrapper<>();
        //前端传来的dto里有一个自媒体用户id，用WmNewSResultDto接受，可以getId()获取自媒体新闻id
        queryWrapper.eq(ApArticle::getWmNewsId, wmNewsId);
        ApArticle apArticle = getOne(queryWrapper);
        //1.保存或修改文章信息
        if (apArticle == null) {
            //新增操作
            apArticle = new ApArticle();
            //根据自媒体用户id查询Author对象，进而得到文章作者id
            ApAuthor apAuthor = apAuthorService.getByWmUserId(dto.getWmUserId());
            if (apAuthor != null) {
                apArticle.setAuthorName(apAuthor.getName());
                apArticle.setAuthorId(apAuthor.getId());
            }
            //普通文章-0 热点文章-1
            apArticle.setFlag(0);
            //评论数
            apArticle.setComments(0L);
            apArticle.setIsForward(true);
            //点赞
            apArticle.setLikes(0L);
            apArticle.setCollection(0L);
            apArticle.setViews(0L);
            apArticle.setCreatedTime(new Date());
            apArticle.setIsComment(true);
            apArticle.setIsDelete(false);
            apArticle.setOrigin(0);
            apArticle.setIsDown(false);
            apArticle.setWmNewsId(wmNewsId);
        }
        apArticle.setImages(dto.getImages());
        apArticle.setLabels(dto.getLabels());
        apArticle.setChannelId(dto.getChannelId());
        apArticle.setChannelName(dto.getChannelName());
        apArticle.setLayout(dto.getType());
        apArticle.setTitle(dto.getTitle());
        //有发布时间按他的来，没有发布时间，立即发布
        apArticle.setPublishTime(dto.getPublishTime() == null ? new Date() : dto.getPublishTime());
        boolean b1 = saveOrUpdate(apArticle);
        if (!b1) {
            log.error("保存或修改文章信息失败");
            throw new LeadException(AppHttpCodeEnum.UPDATE_ERROR);
        }
        //2.保存或修改文章内容
        Long apArticleId = apArticle.getId();
        ApArticleContent apArticleContent = new ApArticleContent();
        apArticleContent.setArticleId(apArticleId);
        apArticleContent.setContent(dto.getContent());
        boolean b2 = apArticleContentService.saveOrUpdate(apArticleContent);
        if (!b2) {
            log.error("保存或修改文章内容失败");
            throw new LeadException(AppHttpCodeEnum.UPDATE_ERROR);
        }

        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                String htmlUrl = articleFreemarkerService.buildContentHtml(apArticleId, dto.getContent());
                log.info("子线程返回的htmlUrl={}", htmlUrl);
                return htmlUrl;
            }
        };


        Future<String> future = taskExecutor.submit(callable);
        try {
            //两秒之内获得该字符串
            String htmlUrl = future.get(2, TimeUnit.SECONDS);
            ApArticle apArticleUrl = new ApArticle();
            apArticleUrl.setStaticUrl(htmlUrl);
            apArticleUrl.setId(apArticleId);
            boolean b = updateById(apArticleUrl);
            if (!b) {
                log.error("静态页面保存失败");
            }
            return apArticleId;
        } catch (Exception e) {
            log.error("获取子线程结果失败");
            e.printStackTrace();
            throw new LeadException(AppHttpCodeEnum.SERVER_ERROR);
        }
    }

    @Override
    public List<ArticleDto> load(ArticleHomeDto dto, int type) {
        Page<ApArticle> apArticlePage = new Page<>(1, dto.getSize());
        LambdaQueryWrapper<ApArticle> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApArticle::getIsDown, false).eq(ApArticle::getIsDelete, false);
        //0-默认推荐频道 ,如果不是推荐频道才需要根据频道Id查询,Java,mysql......
        queryWrapper.eq(dto.getChannelId() != 0, ApArticle::getChannelId, dto.getChannelId());
        //最小发布时间是一个默认很大的值,首页查询要查询文章发布时间小于最小发布时间(mintime)的文章
        if (type == 2) {
            queryWrapper.gt(ApArticle::getPublishTime, dto.getMaxTime());
        } else {
            queryWrapper.lt(ApArticle::getPublishTime, dto.getMinTime());
        }
        IPage<ApArticle> iPage = page(apArticlePage, queryWrapper);
        if (iPage == null || CollectionUtils.isEmpty(iPage.getRecords())) {
            log.error("文章不存在");
            throw new LeadException(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        List<ApArticle> records = iPage.getRecords();
        List<ArticleDto> articleDtos = BeanHelper.copyWithCollection(records, ArticleDto.class);
        return articleDtos;
    }

    @Override
    public List<ArticleDto> findByPage(Integer size, Integer page) {
        Page<ApArticle> apArticlePage = new Page<>(size, page);
        LambdaQueryWrapper<ApArticle> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ApArticle::getIsDelete, false)
                .eq(ApArticle::getIsDown, false);
        IPage<ApArticle> apArticleIPage = page(apArticlePage, lambdaQueryWrapper);
        if (!CollectionUtils.isEmpty(apArticleIPage.getRecords()) && apArticleIPage != null) {
            List<ArticleDto> articleDtos = BeanHelper.copyWithCollection(apArticleIPage.getRecords(), ArticleDto.class);
            return articleDtos;
        }
        return null;
    }
}
