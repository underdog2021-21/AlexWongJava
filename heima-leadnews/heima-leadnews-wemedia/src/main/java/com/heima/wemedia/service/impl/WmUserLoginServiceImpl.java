package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.heima.common.dtos.ResponseResult;
import com.heima.common.enums.AppHttpCodeEnum;
import com.heima.common.exception.LeadException;
import com.heima.common.util.JwtUtils;
import com.heima.model.media.dtos.WmUserLoginDto;
import com.heima.wemedia.entity.WmUser;
import com.heima.wemedia.entity.WmUserLogin;
import com.heima.wemedia.mapper.WmUserLoginMapper;
import com.heima.wemedia.service.WmUserLoginService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.wemedia.service.WmUserService;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 自媒体用户登录行为信息表 服务实现类
 * </p>
 *
 * @author HM
 * @since 2022-09-04
 */
@Service
@Slf4j
public class WmUserLoginServiceImpl extends ServiceImpl<WmUserLoginMapper, WmUserLogin> implements WmUserLoginService {

}
