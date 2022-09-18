package com.heima.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.heima.admin.entity.AdUser;
import com.heima.admin.service.AdUserService;
import com.heima.admin.service.LoginService;
import com.heima.common.dtos.ResponseResult;
import com.heima.common.enums.AppHttpCodeEnum;
import com.heima.common.exception.LeadException;
import com.heima.common.util.BeanHelper;
import com.heima.common.util.JwtUtils;
import com.heima.model.admin.dtos.AdUserDto;
import com.heima.model.admin.dtos.AdUserLoginDto;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Lenovo
 * @Date 2022/9/2 17:05
 * @Version 1.0
 */
@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private AdUserService adUserService;

    @Autowired
    private BCryptPasswordEncoder encoder;


    @Override
    public Map<String, Object> login(AdUserLoginDto adUserLoginDto) {

        //获取用户名密码
        String name = adUserLoginDto.getName();
        String password = adUserLoginDto.getPassword();
        //验证用户名
        LambdaQueryWrapper<AdUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AdUser::getName, name);
        //从数据库查出和这个名字相同的一条数据
        AdUser adUser = adUserService.getOne(lambdaQueryWrapper);

        //如果没有数据，直接返回错误
        if (adUser == null) {
            throw new LeadException(AppHttpCodeEnum.AD_USER_NOT_EXIST);
        }
        //验证密码,使用Bcrypt
        boolean matches = encoder.matches(password, adUser.getPassword());
        //如果密码不对,直接返回错误信息
        if (!matches) {
            throw new LeadException(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
        }
        //生成token(id,过期时间),根据id按照jwt格式生成token,jwt是生成token的一种规范,jwt的主要作用，是防止篡改。
        //jwt定义token的结构  base64(head).base64(payload).signature
        String token = JwtUtils.generateTokenExpireInMinutes(adUser.getId(), 120);
        //返回map
        HashMap<String, Object> map = new HashMap<>();
        AdUserDto adUserDto = BeanHelper.copyProperties(adUser, AdUserDto.class);
        map.put("user", adUserDto);
        map.put("token", token);
        return map;
    }
}
