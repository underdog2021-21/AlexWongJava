package com.heima.user.service;

import com.heima.common.dtos.PageResult;
import com.heima.model.user.dtos.AuthDto;
import com.heima.model.user.dtos.UserAuthDto;
import com.heima.user.entity.ApUserAuth;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * APP实名认证信息表 服务类
 * </p>
 *
 * @author HM
 * @since 2022-09-02
 */
public interface ApUserAuthService extends IService<ApUserAuth> {

    PageResult<UserAuthDto> findByPage(AuthDto authDto);

    void authUser(AuthDto authDto, int status);
}
