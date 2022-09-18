package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.heima.common.constants.message.WmNewsUpDownConstants;
import com.heima.common.dtos.PageResult;
import com.heima.common.enums.AppHttpCodeEnum;
import com.heima.common.exception.LeadException;
import com.heima.common.threadlocal.UserThreadLocalUtils;
import com.heima.common.util.BeanHelper;
import com.heima.common.util.JsonUtils;
import com.heima.model.media.dtos.WmNewsDto;
import com.heima.model.media.dtos.WmNewsPageReqDto;
import com.heima.model.media.dtos.WmNewsResultDTO;
import com.heima.wemedia.entity.WmMaterial;
import com.heima.wemedia.entity.WmNews;
import com.heima.wemedia.mapper.WmNewsMapper;
import com.heima.wemedia.service.WmMaterialService;
import com.heima.wemedia.service.WmNewsAuditService;
import com.heima.wemedia.service.WmNewsMaterialService;
import com.heima.wemedia.service.WmNewsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.core.appender.mom.kafka.KafkaProducerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

import static com.heima.common.constants.message.WmNewsUpDownConstants.WM_NEWS_UP_OR_DOWN_TOPIC;

/**
 * <p>
 * 自媒体图文内容信息表 服务实现类
 * </p>
 *
 * @author HM
 * @since 2022-09-04
 */
@Service
@Slf4j
public class WmNewsServiceImpl extends ServiceImpl<WmNewsMapper, WmNews> implements WmNewsService {
    @Autowired
    private WmNewsAuditService wmNewsAuditService;


    @Override
    public PageResult<WmNewsResultDTO> findByPage(WmNewsPageReqDto dto) {
        Integer userId = UserThreadLocalUtils.getUserId();
        Page<WmNews> page1 = new Page<>(dto.getPage(), dto.getSize());
        LambdaQueryWrapper<WmNews> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper
                .eq(WmNews::getWmUserId, userId)
                .eq(dto.getChannelId() != null, WmNews::getChannelId, dto.getChannelId())
                .like(dto.getKeyword() != null, WmNews::getTitle, dto.getKeyword())
                .gt(dto.getBeginPubDate() != null, WmNews::getCreatedTime, dto.getBeginPubDate())
                .lt(dto.getEndPubDate() != null, WmNews::getPublishTime, dto.getEndPubDate());
        if (dto.getStatus() != null && dto.getStatus() != -1) {
            //数据库中对status字段进行了更细致的划分,
            // 所以应该在这里做前端参数与数据库数据的映射处理
            if (dto.getStatus() == 1) {
                lambdaQueryWrapper.in(WmNews::getStatus, new Integer[]{1, 3});//1,3
            } else if (dto.getStatus() == 4) {
                lambdaQueryWrapper.in(WmNews::getStatus, new Integer[]{4, 8, 9});//4,8,9
            } else {
                lambdaQueryWrapper.eq(WmNews::getStatus, dto.getStatus());
            }
        }
        IPage<WmNews> wmNewsIPage = page(page1, lambdaQueryWrapper);
        List<WmNews> records = wmNewsIPage.getRecords();
        if (wmNewsIPage == null || CollectionUtils.isEmpty(records)) {
            log.error("数据为空");
            throw new LeadException(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        List<WmNewsResultDTO> wmNewsResultDTOS = BeanHelper.copyWithCollection(records, WmNewsResultDTO.class);
        PageResult<WmNewsResultDTO> wmNewsResultDTOPageResult
                = new PageResult<WmNewsResultDTO>(
                dto.getPage(),
                dto.getSize(),
                wmNewsIPage.getTotal(),
                wmNewsResultDTOS);
        return wmNewsResultDTOPageResult;
    }

    @Autowired
    private WmNewsMaterialService wmNewsMaterialService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Override
    public void downOrUp(WmNewsDto dto) {
        Integer userId = UserThreadLocalUtils.getUserId();
        Boolean enable = dto.getEnable();
        Integer newsId = dto.getId();
        WmNews wmNews = getById(newsId);
        if (wmNews == null) {
            log.error("没有数据");
            throw new LeadException(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        if (wmNews.getStatus() != 9) {
            log.error("文章未发布,userId={}",userId);
            throw new LeadException(AppHttpCodeEnum.PARAM_INVALID);
        }
        LambdaUpdateWrapper<WmNews> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(WmNews::getId, newsId).set(WmNews::getEnable, enable?1:0);
        boolean update = update(updateWrapper);
        if (!update) {
            log.error("更新失败");
            throw new LeadException(AppHttpCodeEnum.SERVER_ERROR);
        }
        HashMap<String, Long> map = new HashMap<>();
        Long articleId = wmNews.getArticleId();
        map.put("articleId", articleId);
        map.put("enable", dto.getEnable()?1l:0l);
        //静态导入
        kafkaTemplate.send(WM_NEWS_UP_OR_DOWN_TOPIC, JsonUtils.toString(map));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submit(WmNewsDto dto) {
        Integer userId = UserThreadLocalUtils.getUserId();

        //dto->entity
        //只针对特定部分(新增,提交待审核)修改数据即可
        //其他数据本身在这里,不需额外操作
        WmNews wmNews = BeanHelper.copyProperties(dto, WmNews.class);
        //没传id,是新增操作
        //新增操作,设置用户id,创建时间
        if (dto.getId() == null) {
            wmNews.setCreatedTime(new Date());
            //默认上架
            wmNews.setEnable(true);
        }
        wmNews.setWmUserId(userId);
        //如果是提交待审核,设置提交时间
        if (dto.getStatus() == 1) {
            wmNews.setSubmitedTime(new Date());
        }
        //根据接口文档处理封面图片列表 list->String
        //用流来操作，根据数据库注释，用“，”隔开
        if (!CollectionUtils.isEmpty(dto.getImages())) {
            String str = dto.getImages().stream().collect(Collectors.joining(","));
            wmNews.setImages(str);
        }

        boolean b = saveOrUpdate(wmNews);
        if (!b) {
            log.error("新增,修改,提交文章操作失败");
            throw new LeadException(AppHttpCodeEnum.UPDATE_ERROR);
        }

        //获取文章主键id
        //在文章表和素材表的中间表（wm_news_material）里找到对应信息，进行操做
        //保存文章素材中间表数据
        Integer wmNewsId = wmNews.getId();
        //修改，提交操作，要先删除原有数据，再保存
        if (dto.getId() != null) {
            wmNewsMaterialService.removeByNewsId(wmNewsId);
        }
        //根据接口文档分析，文章的content和images与素材有关
        //获取文章的content使用的素材信息
        List<Integer> idsByNewsContent = getMaterialIdsByNewsContent(wmNews.getContent());
        if (!CollectionUtils.isEmpty(idsByNewsContent)) {
            //内容引用  type-0
            wmNewsMaterialService.saveBatchNewsAndMaterial(wmNewsId, idsByNewsContent, 0);
        }
        //获取文章封面images使用的素材信息
        //前端images不是必传文件,所以要判断一下是否存在
        if (!CollectionUtils.isEmpty(dto.getImages())) {
            List<Integer> idsByNewsImages = getMaterailIdsByNewsImages(wmNews.getImages());
            //主图引用（封面）  type-1
            wmNewsMaterialService.saveBatchNewsAndMaterial(wmNewsId, idsByNewsImages, 1);
        }
        //如果提交审核，就调用审核的代码
        if (dto.getStatus() == 1) {
            wmNewsAuditService.auditWmNews(wmNews);
        }
    }

    @Autowired
    private WmMaterialService wmMaterialService;


    /**
     * @param images
     * @return List<Integer>
     * @Description: 根据images获取id
     * @Author wangzifeng
     * @CreateTime 2022/9/6 12:21
     */
    private List<Integer> getMaterailIdsByNewsImages(String images) {
        LambdaQueryWrapper<WmMaterial> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //where url in(a,b,c)
        lambdaQueryWrapper.in(WmMaterial::getUrl, images);
        //把所有符合条件的放在集合中
        List<WmMaterial> wmMaterialList = wmMaterialService.list(lambdaQueryWrapper);
        //用流的方式映射获取id，再收集成为list
        List<Integer> collect = wmMaterialList.stream()
                .map(WmMaterial::getId)
                .collect(Collectors.toList());
        return collect;
    }

    /**
     * @param content
     * @return List<Integer>
     * @Description: 根据文章内容获取id
     * @Author wangzifeng
     * @CreateTime 2022/9/6 12:21
     */
    private List<Integer> getMaterialIdsByNewsContent(String content) {
        List<Map<String, Object>> list = JsonUtils.nativeRead(content, new TypeReference<List<Map<String, Object>>>() {
        });
        ArrayList<Integer> ids = new ArrayList<>();

        for (Map<String, Object> map : list) {
            String type = map.get("type").toString();
            if ("images".equals(type)) {
                Integer id = Integer.valueOf(map.get("id").toString());
                ids.add(id);
            }
        }

        return ids;
    }


    /**
     * @param id
     * @return WmNewsResultDTO
     * @Description: 根据主键id查询文章
     * @Author wangzifeng
     * @CreateTime 2022/9/6 17:51
     */
    @Override
    public WmNewsResultDTO findById(Integer id) {
        Integer userId = UserThreadLocalUtils.getUserId();
        LambdaQueryWrapper<WmNews> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WmNews::getWmUserId, userId).eq(WmNews::getId, id);
        WmNews wmNews = getOne(lambdaQueryWrapper);
        if (wmNews == null) {
            throw new LeadException(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        WmNewsResultDTO wmNewsDto = BeanHelper.copyProperties(wmNews, WmNewsResultDTO.class);
        return wmNewsDto;
    }


}
