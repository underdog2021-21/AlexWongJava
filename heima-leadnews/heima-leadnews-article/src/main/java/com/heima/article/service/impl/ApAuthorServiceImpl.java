package com.heima.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.heima.article.entity.ApAuthor;
import com.heima.article.mapper.ApAuthorMapper;
import com.heima.article.service.ApAuthorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.enums.AppHttpCodeEnum;
import com.heima.common.exception.LeadException;
import com.heima.model.article.dtos.AuthorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * APP文章作者信息表 服务实现类
 * </p>
 *
 * @author HM
 * @since 2022-09-04
 */
@Slf4j
@Service
public class ApAuthorServiceImpl extends ServiceImpl<ApAuthorMapper, ApAuthor> implements ApAuthorService {


    @Override
    public Integer saveArticleAuthor(AuthorDto dto) {
        //根据ap_user_id查询作者是否存在
        LambdaQueryWrapper<ApAuthor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ApAuthor::getUserId, dto.getUserId());
        ApAuthor apAuthor = getOne(lambdaQueryWrapper);
        //如果作者不存在,新建一个作者
        if (apAuthor == null) {
            apAuthor = new ApAuthor();
            apAuthor.setWmUserId(dto.getWmUserId());
            apAuthor.setCreatedTime(new Date());
        }
        //如果存在就更新
        apAuthor.setName(dto.getName());
        apAuthor.setType(dto.getType());
        boolean b = saveOrUpdate(apAuthor);
        if (!b) {
            log.error("新增修改作者失败，userId={",dto.getUserId()+"}");
            throw new LeadException(AppHttpCodeEnum.SERVER_ERROR);
        }
        return apAuthor.getId();
    }

    @Override
    public ApAuthor getByWmUserId(Integer wmUserId) {
        LambdaQueryWrapper<ApAuthor> apAuthorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        apAuthorLambdaQueryWrapper.eq(wmUserId!=null,ApAuthor::getWmUserId,wmUserId);
        ApAuthor apAuthor = getOne(apAuthorLambdaQueryWrapper);
        return apAuthor;
    }
}
