package com.heima.article.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.heima.article.service.ArticleFreemarkerService;
import com.heima.common.enums.AppHttpCodeEnum;
import com.heima.common.exception.LeadException;
import com.heima.file.service.MinioService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class ArticleFreemarkerServiceImpl implements ArticleFreemarkerService {

    @Autowired
    private Configuration configuration;
    @Autowired
    private MinioService minioService;

    @Override
    public String buildContentHtml(Long articleId, String content) {
        try {
            //获取模板页面
            Template template = configuration.getTemplate("article.ftl");
            Map<String, Object> map = new HashMap<>();
            //准备数据模型
            map.put("content", JSONArray.parseArray(content));
            //生成静态页面
            StringWriter stringWriter = new StringWriter();
            template.process(map,stringWriter);
            String fileName = articleId + ".html";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(stringWriter.toString().getBytes());
            //上传到minio里
            String url = minioService.uploadHtmlFile("", fileName, inputStream);
            log.info("页面url={}", url);

            return url;
        } catch (Exception e) {
            e.printStackTrace();
            throw new LeadException(AppHttpCodeEnum.SERVER_ERROR);
        }
    }
}
