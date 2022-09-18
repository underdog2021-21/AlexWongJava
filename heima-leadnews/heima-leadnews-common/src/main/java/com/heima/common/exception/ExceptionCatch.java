package com.heima.common.exception;

import com.heima.common.dtos.ResponseResult;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Lenovo
 * @Date 2022/9/1 17:44
 * @Version 1.0
 */
@RestControllerAdvice
public class ExceptionCatch {

    @ExceptionHandler(RuntimeException.class)
    public ResponseResult runtimeException(RuntimeException e) {
        return ResponseResult.error(400, e.getMessage());
    }

    /**
     * 处理自定义异常,防止把状态码写死了
     *
     * @param e
     * @return
     */
    @ExceptionHandler(LeadException.class)
    public ResponseResult leadException(LeadException e) {

        return ResponseResult.error(e.getStatus(), e.getMessage());
    }

    /**
     * @Description: 对账号密码为空或者长度超长时的异常处理(validition参数验证),将其处理成前端要求的格式
     * @param e
     * @return ResponseResult
     * @Author wangzifeng
     * @CreateTime 2022/9/2 17:59
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseResult methodArgumentNotValidException(MethodArgumentNotValidException e) {
//        获取所有错误信息
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        String msg = "";
        if (!CollectionUtils.isEmpty(allErrors)) {
            //            使用流获取结果


            msg = allErrors.stream()
                    .map(ObjectError::getDefaultMessage)
                    //用逗号分隔
                    .collect(Collectors.joining(","));

        }
        return ResponseResult.error(400, msg);
    }
}
