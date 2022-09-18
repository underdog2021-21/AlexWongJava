package com.heima.article.client;

import com.heima.model.article.dtos.AuthorDto;
import com.heima.model.media.dtos.WmNewsResultDTO;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author Lenovo
 * @Date 2022/9/4 13:30
 * @Version 1.0
 */
@FeignClient("leadnews-article")
public interface ArticleFeign {
    @PostMapping("/api/v1/author/save")
    Integer saveArticleAuthor(@RequestBody AuthorDto dto);

    @PostMapping("/api/v1/article/save")
    Long saveArticle(@RequestBody WmNewsResultDTO dto);
}
