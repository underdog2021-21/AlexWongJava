package com.heima.wemedia.service;

import com.heima.common.dtos.ResponseResult;
import com.heima.model.media.dtos.WmUserDto;
import com.heima.wemedia.entity.WmUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 自媒体用户信息表 服务类
 * </p>
 *
 * @author HM
 * @since 2022-09-04
 */
public interface WmUserService extends IService<WmUser> {

    Integer saveWmUser(WmUserDto wmUserDto);


}
