package com.heima.user.service;

import com.heima.model.user.dtos.AppLoginDto;
import com.heima.user.entity.ApUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * APP用户信息表 服务类
 * </p>
 *
 * @author HM
 * @since 2022-09-02
 */
public interface ApUserService extends IService<ApUser> {

    Map<String,Object> loginApp(AppLoginDto dto);
}
