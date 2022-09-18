package com.heima.user.controller;

import com.heima.common.dtos.ResponseResult;
import com.heima.model.user.dtos.AppLoginDto;
import com.heima.user.service.ApUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;


@RestController
public class LoginController {
    @Autowired
    private ApUserService apUserService;

    @PostMapping("/api/v1/login/login_auth/")
    public ResponseResult<Map<String, Object>> loginApp(@Valid @RequestBody AppLoginDto dto) {

        return ResponseResult.ok(apUserService.loginApp(dto));
    }
}
