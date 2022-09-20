package com.heima.search.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;

import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.CreateResponse;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import com.fasterxml.jackson.core.type.TypeReference;
import com.heima.common.enums.AppHttpCodeEnum;
import com.heima.common.exception.LeadException;
import com.heima.common.util.JsonUtils;
import com.heima.model.article.dtos.ArticleDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.client.RequestOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ApUserSearchServiceImpl implements com.heima.search.service.ApUserSearchService {

    @Autowired
    private ElasticsearchClient client;

    private String indexName = "app_info_article";
    @Override
    public void creatIndexBatch(List<ArticleDto> articleDtoList) {
        // 构建一个批量数据集合
        List<BulkOperation> list = new ArrayList<>();
        System.out.println("========================"+articleDtoList.size());
        for (ArticleDto articleDto : articleDtoList) {
            List<Map<String, Object>> content = JsonUtils.nativeRead(
                    articleDto.getContent(),
                    new TypeReference<List<Map<String, Object>>>() {
                    });
            String contentText = "";
            if (!CollectionUtils.isEmpty(content)) {
                for (Map<String, Object> map1 : content) {
                    String type = map1.get("type").toString();
                    if ("text".equals(type)) {
                        String value = map1.get("value").toString();
                        if (contentText.length() > 0) {
                            contentText += ",";
                        }
                        contentText += value;
                    }
                    articleDto.setContent(contentText);
                }
            }
            list.add(new BulkOperation.Builder().create(
                    d -> d.document(articleDto).id(articleDto.getId().toString()).index(indexName)).build());
        }
        try {
            client.bulk(e -> e.index(indexName).operations(list));
        } catch (Exception e) {
            log.error("db数据导入Es异常！！");
            e.printStackTrace();
            throw new LeadException(AppHttpCodeEnum.SERVER_ERROR);
        }
    }
}
