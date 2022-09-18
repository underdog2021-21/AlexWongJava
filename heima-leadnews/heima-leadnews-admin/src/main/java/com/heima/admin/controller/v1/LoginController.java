package com.heima.admin.controller.v1;

import com.heima.admin.entity.AdUserLogin;
import com.heima.admin.service.LoginService;
import com.heima.common.dtos.ResponseResult;
import com.heima.model.admin.dtos.AdUserLoginDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

/**
 * @Author Lenovo
 * @Date 2022/9/2 17:01
 * @Version 1.0
 */
@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;


    //@Valid自动验证,对应的dto里还有注释
    @PostMapping("/login/in")
    public ResponseResult<Map<String, Object>> login(@Valid @RequestBody AdUserLoginDto adUserLoginDto) {
        return ResponseResult.ok(loginService.login(adUserLoginDto));
    }

}
