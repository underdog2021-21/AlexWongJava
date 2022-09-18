package com.heima.article.service;

import com.heima.article.entity.ApArticle;

public interface ArticleFreemarkerService {

    /**
     * 生成文章详情静态页面
     * 写入Minio
     * @param articleId
     * @param content
     * @return String 返回的是一个静态页面的路径
     */
    String buildContentHtml(Long articleId,String content);
}