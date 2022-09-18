package com.heima.wemedia.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.heima.article.client.ArticleFeign;
import com.heima.common.aliyun.GreenImageUploadScan;
import com.heima.common.aliyun.GreenTextScan;
import com.heima.common.delayTask.RedisDelayedQueueUtil;
import com.heima.common.enums.AppHttpCodeEnum;
import com.heima.common.exception.LeadException;
import com.heima.common.util.BeanHelper;
import com.heima.common.util.JsonUtils;
import com.heima.common.util.SensitiveWordUtil;
import com.heima.file.service.MinioService;
import com.heima.model.media.dtos.WmNewsResultDTO;
import com.heima.wemedia.entity.WmNews;
import com.heima.wemedia.entity.WmSensitive;
import com.heima.wemedia.service.WmNewsAuditService;
import com.heima.wemedia.service.WmNewsService;
import com.heima.wemedia.service.WmSensitiveService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.lang.invoke.LambdaConversionException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * @Author Lenovo
 * @Date 2022/9/11 23:59
 * @Version 1.0
 */
@Slf4j
@Service
public class WmNewsAuditServiceImpl implements WmNewsAuditService {
    @Autowired
    private WmNewsService wmNewsService;
    @Autowired
    private MinioService minioService;
    @Autowired
    private GreenTextScan greenTextScan;
    @Autowired
    private GreenImageUploadScan greenImageUploadScan;
    @Autowired
    private ArticleFeign articleFeign;
    @Autowired
    private RedisDelayedQueueUtil delayedQueueUtil;

    @Override
    @Async //把审核放到子线程里
    @GlobalTransactional(rollbackFor = Exception.class)
    public void auditWmNews(WmNews wmNews) {
        //检查文章状态，必须是状态是 1-待审核
        if (wmNews.getStatus() != 1) {

            log.error("文章状态不是待审核，结束,id={}", wmNews.getId());
            return;
        }
        Integer wmNewsId = wmNews.getId();
        //获取文章的 文本内容（title,content） 和 图片内容 （content，images）
        Map<String, Object> map = getTextAndImage(wmNews);
        String text = (String) map.get("text");
        Set<String> image = (Set<String>) map.get("image");
        //调用阿里云文本审核接口，如果审核不通过，改状态2，如果不确定改状态3
        boolean b1 = checkText(text, wmNewsId);
        if (!b1) {
            log.info("文本检测未通过,结束");
            return;
        }

        //调用阿里云图片审核接口，如果审核不通过，改状态2，如果不确定改状态3
        if (!CollectionUtils.isEmpty(image)) {
            boolean b2 = checkImage(image, wmNewsId);
            if (!b2) {
                log.info("文本检测未通过,结束");
                return;
            }
        }
        boolean b3=checkSensitiveWord(text,wmNewsId);
        if (!b3) {
            log.info("敏感词检测未通过");
            return;
        }

        //审核通过后，修改文章状态 8-审核通过，待发布
        updateWmNewsStatus(wmNewsId, 8, null,null);
        //判断是否到发布时间,如果没到发布时间,结束
        Date publishTime = wmNews.getPublishTime();
        if (publishTime != null &&
                System.currentTimeMillis() < publishTime.getTime()) {
            log.info("没有到发布时间，放入延迟任务，wmNewsId={}，pubtime={}",wmNewsId,publishTime.getTime());
            //获取延迟时间
            Long delayTime = publishTime.getTime()- System.currentTimeMillis();
            log.info("wmNewsId={}，延迟时间 delay毫秒={}",wmNewsId,delayTime);
            //day50:把当前文章放入延迟队列
            delayedQueueUtil.addQueue(wmNewsId,delayTime, TimeUnit.MILLISECONDS,"wm.news.pub");
            log.info("没有到发布时间,结束");
            return;
        }

        publishWmNews(wmNews,1);

    }

    /**
     * 发布文章
     * @param wmNews
     * @param type 1-从审核方法调用,无需重新查询对象
     *             2-从定时任务调用,需要重新查询wmNews对象
     */
    @Override
    public void publishWmNews(WmNews wmNews, int type) {
        Integer wmNewsId = wmNews.getId();
        if (type == 2) {
            wmNews= wmNewsService.getById(wmNewsId);
        }
        log.info("======调用文章发布方法======");
        //只要是远程调用，最好都try/catch一下
        try {
            if (wmNews == null) {
                return;
            }
            WmNewsResultDTO wmNewsResultDTO = BeanHelper.copyProperties(wmNews, WmNewsResultDTO.class);
            //发布自媒体文章,远程调用article服务
            Long articleId = articleFeign.saveArticle(wmNewsResultDTO);
            //修改文章状态,9-已发布
            updateWmNewsStatus(wmNewsId, 9, null, articleId);
        } catch (Exception e) {
            log.error("远程调用服务失败");
            e.printStackTrace();
            throw new LeadException(AppHttpCodeEnum.SERVER_ERROR);
        }
    }

    @Autowired
    private WmSensitiveService wmSensitiveService;
    /**
     * 审核敏感词
     * @param text
     * @param wmNewsId
     * @return
     */
    private boolean checkSensitiveWord(String text, Integer wmNewsId) {
        List<WmSensitive> list = wmSensitiveService.list();
        List<String> words = list.stream().map(WmSensitive::getSensitives).collect(Collectors.toList());
        Map<String, Integer> map = SensitiveWordUtil.matchWords(words, text);
        if (!org.springframework.util.CollectionUtils.isEmpty(map)) {
            //把敏感词按逗号分割，组成一个字符串，作为审核不通过的理由
            String reason = map.keySet().stream().collect(Collectors.joining(","));
            updateWmNewsStatus(wmNewsId,2,reason,null);
            return false;
        }
        return true;
    }

    /**
     * 阿里云检测图片
     *
     * @param image
     * @param wmNewsId
     * @return
     */
    private boolean checkImage(Set<String> image, Integer wmNewsId) {
        ArrayList<byte[]> list = new ArrayList<>();
        for (String s : image) {
            byte[] bytes = minioService.downLoadFile(s);
            list.add(bytes);
        }
        try {
            Map<String, String> map = greenImageUploadScan.imageScan(list);
            String suggestion = map.get("suggestion");
            String label = map.get("label");
            if ("pass".equals(suggestion)) {
                log.info("图片审核通过");
                return true;
            } else if ("review".equals(suggestion)) {
                log.info("需提交人工审核");
                updateWmNewsStatus(wmNewsId, 3, label,null);
                return false;
            } else {
                log.error("审核不通过");
                updateWmNewsStatus(wmNewsId, 2, label,null);
                return false;
            }
        } catch (Exception e) {
            log.error("图片检测失败");
            throw new LeadException(AppHttpCodeEnum.SERVER_ERROR);
        }
    }

    private void updateWmNewsStatus(Integer id, int status, String reason,Long articleId) {
        WmNews wmNews = new WmNews();
        wmNews.setId(id);
        wmNews.setStatus(status);
        if (StringUtils.isNotBlank(reason)) {
            wmNews.setReason(reason);
        }
        if (articleId != null) {
            wmNews.setArticleId(articleId);
        }
        wmNewsService.updateById(wmNews);
    }

    /**
     * 阿里云检测文本内容
     *
     * @param text
     * @return
     */
    private boolean checkText(String text, Integer wmNewsId) {
        if (StringUtils.isBlank(text)) {
            return true;
        }
        try {
            Map<String, String> map = greenTextScan.greenTextScan(text);
            if (CollectionUtils.isEmpty(Collections.singleton(map))) {
                log.error("阿里云文本检测失败,返回内容错误");
                throw new LeadException(AppHttpCodeEnum.SERVER_ERROR);
            }
            String suggestion = map.get("suggestion");
            String reason = map.get("reason");
            if ("pass".equals(suggestion)) {

                log.info("文本检测通过!");
                return true;
            } else if ("review".equals(suggestion)) {
                log.info("需要人工审核");
                updateWmNewsStatus(wmNewsId, 3, reason,null);
                return false;
            } else {
                updateWmNewsStatus(wmNewsId, 2, reason,null);
                return false;
            }
        } catch (Exception e) {
            log.error("阿里云文本检测失败");
            e.printStackTrace();
            throw new LeadException(AppHttpCodeEnum.SERVER_ERROR);
        }
    }

    private Map<String, Object> getTextAndImage(WmNews wmNews) {
        //文本内容放一个字符串就可以了,图片url相同图片只需要审核一次,放进set里就行
        StringBuilder stringBuilder = new StringBuilder();
        Set<String> urlSet = new HashSet<>();
        String content = wmNews.getContent();
        stringBuilder.append(wmNews.getTitle());
        //content里面有图片,有文字,是个Json字符串
        //Json转Java对象
        if (StringUtils.isNotBlank(content)) {
            List<Map<String, Object>> list = JsonUtils.nativeRead(content, new TypeReference<List<Map<String, Object>>>() {
            });
            for (Map<String, Object> map : list) {
                String type = map.get("type").toString();
                String value = map.get("value").toString();
                if ("text".equals(type)) {
                    stringBuilder.append(",").append(value);
                } else {
                    urlSet.add(value);
                }
            }
        }
        if (StringUtils.isNotBlank(wmNews.getImages())) {
            //当时存储的时候是用逗号把url分开的,这里可以用逗号把它们分开
            String[] urls = wmNews.getImages().split(",");
            //addAll要求传入一个集合,这里用这个方法
            urlSet.addAll(Arrays.asList(urls));
        }
        //构造返回对象
        Map<String, Object> result = new HashMap<>();
        result.put("text", stringBuilder.toString());
        result.put("image", urlSet);

        return result;
    }
}
