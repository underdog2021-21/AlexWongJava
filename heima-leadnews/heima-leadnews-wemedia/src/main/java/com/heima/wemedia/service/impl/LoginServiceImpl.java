package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.heima.common.enums.AppHttpCodeEnum;
import com.heima.common.exception.LeadException;
import com.heima.common.util.JwtUtils;
import com.heima.model.media.dtos.WmUserLoginDto;
import com.heima.wemedia.entity.WmUser;
import com.heima.wemedia.service.LoginService;
import com.heima.wemedia.service.WmUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Lenovo
 * @Date 2022/9/5 17:31
 * @Version 1.0
 */
@Service
@Slf4j
public class LoginServiceImpl implements LoginService {
    @Autowired
    private WmUserService wmUserService;
    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public Map<String, Object> login(WmUserLoginDto dto) {
        //根据用户名name,查询用户是否存在
        LambdaQueryWrapper<WmUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WmUser::getName, dto.getName());
        WmUser wmUser = wmUserService.getOne(lambdaQueryWrapper);
        //用户不存在
        if (wmUser == null) {
            log.error("用户不存在");
            throw new LeadException(AppHttpCodeEnum.WM_USER_DATA_NOT_EXIST);
        }
        //匹配传过来的密码是否与数据库中一致
        boolean matches = encoder.matches(dto.getPassword(), wmUser.getPassword());
        //匹配失败,抛出异常
        if (!matches) {
            log.error("密码错误");
            throw new LeadException(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
        }
        //生成token字符串
        //注意这里的id是wmuser的id,谁登录,就要谁的id,不要传错
        String token = JwtUtils.generateTokenExpireInMinutes(wmUser.getId(), 120);
        Map<String, Object> map = new HashMap<>();
        //密码属于敏感信息,不可以返回给前端,所以这里把这个对象里的密码替换成null
        //这里和数据库无关哦
        wmUser.setPassword(null);
        //接口文档需要user和token
        map.put("user", wmUser);
        map.put("token",token );

        return map;
    }
}
