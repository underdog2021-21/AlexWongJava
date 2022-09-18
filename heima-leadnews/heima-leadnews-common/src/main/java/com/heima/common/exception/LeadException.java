package com.heima.common.exception;

import com.heima.common.enums.AppHttpCodeEnum;
import lombok.Getter;

/**
 * @Author Lenovo
 * @Date 2022/9/1 17:45
 * @Version 1.0
 */
@Getter
public class LeadException extends RuntimeException {

    private Integer status;

    public LeadException(Integer status,String message) {
        super(message);
        this.status = status;
    }

    public LeadException(Integer status,String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }


    //引入枚举异常防止message和code写死了
    public LeadException(AppHttpCodeEnum enums) {
        super(enums.getErrorMessage());
        this.status = enums.getCode();
    }
}
