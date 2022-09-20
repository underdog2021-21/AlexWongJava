package com.heima.search.service;

import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.search.dtos.UserSearchDto;

import java.util.List;

public interface ApUserSearchService {
    /**
     * 批量导入索引
     * @param articleDtoList
     */
    void creatIndexBatch(List<ArticleDto> articleDtoList);

    List<ArticleDto> search(UserSearchDto dto);

    void db2ES();
}
