package com.heima.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.heima.common.enums.AppHttpCodeEnum;
import com.heima.common.exception.LeadException;
import com.heima.common.util.JwtUtils;
import com.heima.model.user.dtos.AppLoginDto;
import com.heima.user.entity.ApUser;
import com.heima.user.mapper.ApUserMapper;
import com.heima.user.service.ApUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * APP用户信息表 服务实现类
 * </p>
 *
 * @author HM
 * @since 2022-09-02
 */
@Service
@Slf4j
public class ApUserServiceImpl extends ServiceImpl<ApUserMapper, ApUser> implements ApUserService {
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Override
    public Map<String, Object> loginApp(AppLoginDto dto) {
        //判断是手机号密码登录还是设备登录
        Map<String, Object> result = new HashMap<>();
        Integer userId = 0;
        if (StringUtils.isNotBlank(dto.getPhone()) &&
                StringUtils.isNotBlank(dto.getPassword())) {
            //设备登录
            LambdaQueryWrapper<ApUser> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ApUser::getPhone,dto.getPhone());

            ApUser apUser = getOne(queryWrapper);
            if (apUser == null) {
                log.error("用户不存在，phone={}",dto.getPhone());
                throw new LeadException(AppHttpCodeEnum.PARAM_INVALID);
            }
            boolean b = encoder.matches(dto.getPassword(),apUser.getPassword());
            if (!b) {
                log.error("密码不正确，phone={}",dto.getPhone());
                throw new LeadException(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
            }
            userId = apUser.getId();
            result.put("user", apUser);
        }
        String token = JwtUtils.generateTokenExpireInMinutes(userId, 120);
        result.put("token", token);

        return result;
    }
}
