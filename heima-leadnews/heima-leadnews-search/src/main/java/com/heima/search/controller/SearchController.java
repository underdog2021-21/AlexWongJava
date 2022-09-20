package com.heima.search.controller;

import com.heima.common.dtos.ResponseResult;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.search.dtos.UserSearchDto;
import com.heima.search.service.ApUserSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class SearchController {
    @Autowired
    private ApUserSearchService apUserSearchService;
    @PostMapping("/api/v1/article/search/search")
    public ResponseResult<List<ArticleDto>> search(@RequestBody UserSearchDto dto) {
        return ResponseResult.ok(apUserSearchService.search(dto));
    }
}
