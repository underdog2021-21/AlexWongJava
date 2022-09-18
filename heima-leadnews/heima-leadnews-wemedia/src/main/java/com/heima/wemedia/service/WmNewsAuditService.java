package com.heima.wemedia.service;

import com.heima.wemedia.entity.WmNews;

/**
 * @Author Lenovo
 * @Date 2022/9/11 23:58
 * @Version 1.0
 */
public interface WmNewsAuditService {
    /**
     * 自媒体文章审核
     * @param wmNews  自媒体文章
     */
    void auditWmNews(WmNews wmNews);

    /**
     * 发布文章
     * @param wmNews
     * @param type  1- 从审核方法调用，无需重新查询wmNews对象
     *              2- 从定时任务调用，需要重新查询wmNews对象
     */
    void publishWmNews(WmNews wmNews, int type);
}
