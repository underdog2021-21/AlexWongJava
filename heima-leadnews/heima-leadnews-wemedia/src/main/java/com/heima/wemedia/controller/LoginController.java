package com.heima.wemedia.controller;

import com.heima.common.dtos.ResponseResult;
import com.heima.model.media.dtos.WmUserDto;
import com.heima.model.media.dtos.WmUserLoginDto;
import com.heima.wemedia.entity.WmUser;
import com.heima.wemedia.entity.WmUserLogin;
import com.heima.wemedia.service.LoginService;
import com.heima.wemedia.service.WmUserLoginService;
import com.heima.wemedia.service.WmUserService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author Lenovo
 * @Date 2022/9/5 12:11
 * @Version 1.0
 */
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login/in")
    public ResponseResult<Map<String, Object>> login(@RequestBody WmUserLoginDto dto) {
        return ResponseResult.ok(loginService.login(dto));
    }

}
