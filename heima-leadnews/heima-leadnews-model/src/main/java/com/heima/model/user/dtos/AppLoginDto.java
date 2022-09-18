package com.heima.model.user.dtos;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AppLoginDto {

    /**
     * 设备id
     */
    @NotNull(message = "设备号不能为空")
    private String equipmentId;
    /**
     * 用户电话号码
     */
    private String phone;
    /**
     * 用户密码
     */
    private String password;
}