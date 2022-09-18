package com.heima.wemedia.service;

import com.heima.model.media.dtos.WmUserLoginDto;

import java.util.Map;

/**
 * @Author Lenovo
 * @Date 2022/9/5 17:31
 * @Version 1.0
 */
public interface LoginService {
    Map<String, Object> login(WmUserLoginDto dto);
}
