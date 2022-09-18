package com.heima.admin.service;

import com.heima.model.admin.dtos.AdUserLoginDto;

import java.util.Map;

/**
 * @Author Lenovo
 * @Date 2022/9/2 17:04
 * @Version 1.0
 */
public interface LoginService {
    Map<String ,Object> login(AdUserLoginDto adUserLoginDto) ;
}
