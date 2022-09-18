package com.heima.article.service;

import com.heima.article.entity.ApArticle;
import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.common.dtos.ResponseResult;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.article.dtos.AuthorDto;
import com.heima.model.media.dtos.WmNewsResultDTO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 文章信息表，存储已发布的文章 服务类
 * </p>
 *
 * @author HM
 * @since 2022-09-04
 */
public interface ApArticleService extends IService<ApArticle> {

    /**
     * 保存文章
     * @param dto
     * @return
     */
    Long saveArticle(WmNewsResultDTO dto);

    /**
     *
     * @param dto
     * @param type 1-首页/上拉 2-下拉
     * @return
     */
   List<ArticleDto> load(ArticleHomeDto dto,int type);

    void upOrDown(Map<String, Long> map);
}
