package com.heima.article.service;

import com.heima.article.entity.ApAuthor;
import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.article.dtos.AuthorDto;

import java.util.Map;

/**
 * <p>
 * APP文章作者信息表 服务类
 * </p>
 *
 * @author HM
 * @since 2022-09-04
 */
public interface ApAuthorService extends IService<ApAuthor> {

    Integer saveArticleAuthor(AuthorDto dto);

    ApAuthor getByWmUserId(Integer wmUserId);

}
