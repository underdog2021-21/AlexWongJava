package com.heima.article.controller;

import com.heima.article.service.ApArticleService;
import com.heima.common.dtos.ResponseResult;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.media.dtos.WmNewsResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class ApArticleController {
    @Autowired
    private ApArticleService apArticleService;
    /**
     * @return:数据库里是Big int，这里用long
     */
    @PostMapping("/api/v1/article/save")
    public Long saveArticle(@RequestBody WmNewsResultDTO dto) {
        return apArticleService.saveArticle(dto);
    }


    @PostMapping("/api/v1/article/load")
    public ResponseResult<List<ArticleDto>> load(@RequestBody ArticleHomeDto dto) {
        return ResponseResult.ok(apArticleService.load(dto,1));
    }

    @PostMapping("/api/v1/article/loadmore")
    public ResponseResult<List<ArticleDto>> loadmore(@RequestBody ArticleHomeDto dto) {
        return ResponseResult.ok(apArticleService.load(dto,1));
    }

    @PostMapping("/api/v1/article/loadnew")
    public ResponseResult<List<ArticleDto>> loadnew(@RequestBody ArticleHomeDto dto) {
        return ResponseResult.ok(apArticleService.load(dto,2));
    }

    @GetMapping("/api/v1/article/findByPage")
    public List<ArticleDto> findByPage(@RequestParam("page") Integer page,@RequestParam("size") Integer size) {
        return apArticleService.findByPage(page,size);
    }
}
