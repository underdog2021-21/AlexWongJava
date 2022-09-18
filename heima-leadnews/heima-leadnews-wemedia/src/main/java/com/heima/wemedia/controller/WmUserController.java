package com.heima.wemedia.controller;


import com.heima.model.media.dtos.WmUserDto;
import com.heima.wemedia.service.WmUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 自媒体用户信息表 前端控制器
 * </p>
 *
 * @author HM
 * @since 2022-09-04
 */
@RestController
public class WmUserController {
    @Autowired
    private WmUserService wmUserService;

    /**
     * @Description: 保存自媒体用户,注意这里是微服务之间调用方法,所以返回值可以为Integer
     * @param wmUserDto
     * @return Integer
     * @Author wangzifeng
     * @CreateTime 2022/9/4 13:19
     */
    @PostMapping("/api/v1/wmuser/save")
    public Integer saveWmUser(@RequestBody WmUserDto wmUserDto) {
        return wmUserService.saveWmUser(wmUserDto);
    }

}

