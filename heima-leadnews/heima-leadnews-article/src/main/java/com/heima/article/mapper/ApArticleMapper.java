package com.heima.article.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heima.article.entity.ApArticle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heima.model.article.dtos.ArticleDto;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 文章信息表，存储已发布的文章 Mapper 接口
 * </p>
 *
 * @author HM
 * @since 2022-09-04
 */
public interface ApArticleMapper extends BaseMapper<ApArticle> {

    @Select("select aa.* ,apc.content from ap_article aa,ap_article_content apc " +
            " where aa.is_delete =0 and aa.is_down =0 and aa.id=apc.article_id ")
    IPage<ArticleDto> selectArticleAndContentPage(Page<ApArticle> apArticlePage);
}
