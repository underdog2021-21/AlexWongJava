package com.heima.user.controller;

import com.heima.common.dtos.PageResult;
import com.heima.common.dtos.ResponseResult;
import com.heima.model.user.dtos.AuthDto;
import com.heima.model.user.dtos.UserAuthDto;
import com.heima.user.service.ApUserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Lenovo
 * @Date 2022/9/4 0:10
 * @Version 1.0
 */
@RestController
public class UserAuthController {
    @Autowired
    private ApUserAuthService apUserAuthService;

    /**
     * @param authDto
     * @return PageResult<UserAuthDto>
     * @Description: 分页查询
     * @Author wangzifeng
     * @CreateTime 2022/9/4 11:15
     */
    @PostMapping("/api/v1/auth/list")
    public PageResult<UserAuthDto> findByPage(@RequestBody AuthDto authDto) {

        return apUserAuthService.findByPage(authDto);
    }

    /**
     * @Description: 审核通过,修改审核状态为9: 1待审核 2审核失败 9审核通过
     * @param authDto
     * @return ResponseResult
     * @Author wangzifeng
     * @CreateTime 2022/9/4 11:21
     */
    @PostMapping("/api/v1/auth/authPass")
    public ResponseResult authPass(@RequestBody AuthDto authDto) {

        apUserAuthService.authUser(authDto,9);

        return ResponseResult.ok();
    }
    /**
     * @Description: 审核不通过,修改审核状态为2: 1待审核 2审核失败 9审核通过
     * @param authDto
     * @return ResponseResult
     * @Author wangzifeng
     * @CreateTime 2022/9/4 11:21
     */
    @PostMapping("/api/v1/auth/authFail")
    public ResponseResult authFail(@RequestBody AuthDto authDto) {

        apUserAuthService.authUser(authDto,2);

        return ResponseResult.ok();
    }

}
