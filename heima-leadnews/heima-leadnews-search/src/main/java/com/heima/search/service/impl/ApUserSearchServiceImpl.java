package com.heima.search.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.*;

import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.search.HighlightField;
import co.elastic.clients.json.JsonData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.heima.article.client.ArticleFeign;
import com.heima.common.enums.AppHttpCodeEnum;
import com.heima.common.exception.LeadException;
import com.heima.common.util.JsonUtils;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.search.dtos.UserSearchDto;
import com.heima.search.service.ApUserSearchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ApUserSearchServiceImpl implements com.heima.search.service.ApUserSearchService {

    @Autowired
    private ElasticsearchClient client;

    private String indexName = "app_info_article";
    private HighlightField.Builder highlightBuilder;

    @Override
    public void creatIndexBatch(List<ArticleDto> articleDtoList) {
        // 构建一个批量数据集合
        List<BulkOperation> list = new ArrayList<>();
        System.out.println("========================" + articleDtoList.size());
        for (ArticleDto articleDto : articleDtoList) {
            List<Map<String, Object>> content = JsonUtils.nativeRead(
                    articleDto.getContent(),
                    new TypeReference<List<Map<String, Object>>>() {
                    });
            String contentText = "";
            if (!CollectionUtils.isEmpty(content)) {
                for (Map<String, Object> map1 : content) {
                    String type = map1.get("type").toString();
                    if ("text" .equals(type)) {
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

    @Override
    public List<ArticleDto> search(UserSearchDto dto) {
        //用户输入的关键词
        String searchWords = dto.getSearchWords();
        int fromIndex = dto.getFromIndex();
        List<ArticleDto> articleDtoList = new ArrayList<>();
        // 组合查询
        try {
            //发送给Es服务端
            SearchResponse<ArticleDto> searchResponse = client.search(
                    s -> {
                        s.index(indexName);
                        s.query(q -> q.bool(b -> {
                                    if (StringUtils.isBlank(searchWords)) {
                                        b.must(m -> m.matchAll(a -> a));
                                    } else {
                                        b.must(c -> c.match(v -> v.query(searchWords).field("title").field("content").operator(Operator.And)));
                                    }
                                    if (dto.getMinBehotTime() != null) {
                                        b.filter(d -> d.range(r -> r.field("publishTime").lt(JsonData.of(dto.getMinBehotTime()))));
                                    }
                                    return b;
                                }
                        ));

                        s.from(fromIndex);
                        s.size(dto.getPageSize());
                        if (StringUtils.isNotBlank(searchWords)) {
                            s.query(q -> q.bool(b -> b.must(c -> c.match(v -> v.query(searchWords).field("title").operator(Operator.And)))))
                                    .highlight(h -> h.fields("title", f -> f.preTags("<font style='color: red; font-size: inherit;'>").postTags("</font>")));
                        }
                        return s;
                    }
                    , ArticleDto.class);
            searchResponse.hits().hits().stream().forEach(
                    f -> {
                        //更简洁
                        ArticleDto articleDto = f.source();
                        if (StringUtils.isNotBlank(searchWords)) {
//                获取高亮结果
                            Map<String, List<String>> highlight = f.highlight();
                            List<String> title = highlight.get("title");
                            String newTitle = StringUtils.join(title);

//                高亮后的标题
                            articleDto.setTitle(newTitle);
                        }
                        articleDtoList.add(articleDto);
                    }
            );
        } catch (Exception e) {
            log.error("关键词搜索异常");
            e.printStackTrace();
            throw new LeadException(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        //解析Es的返回数据
        //构造返回结果
        return articleDtoList;
    }

    @Autowired
    private ArticleFeign articleFeign;
    @Autowired
    private ApUserSearchService apUserSearchService;

    @Override
    public void db2ES() {
        int page = 1, size = 5;
        while (true) {
//        远程调用article服务，获取文章数据
            List<ArticleDto> articleDtoList = articleFeign.findByPage(page, size);
            if (org.springframework.util.CollectionUtils.isEmpty(articleDtoList)) {
                break;
            }
//        调用方法导入Es
            creatIndexBatch(articleDtoList);
            if (articleDtoList.size() < size) {
                break;
            }
            page++;
        }
    }
}
