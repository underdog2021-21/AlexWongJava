package com.heima.article.controller;


import com.heima.article.entity.ApAuthor;
import com.heima.article.service.ApArticleService;
import com.heima.article.service.ApAuthorService;
import com.heima.model.article.dtos.AuthorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * APP文章作者信息表 前端控制器
 * </p>
 *
 * @author HM
 * @since 2022-09-04
 */
@RestController
public class ApAuthorController {
    @Autowired
    private ApAuthorService apArticleService;

    @PostMapping("/api/v1/author/save")
    public Integer saveArticleAuthor(@RequestBody AuthorDto dto) {
        return apArticleService.saveArticleAuthor(dto);
    }
}

