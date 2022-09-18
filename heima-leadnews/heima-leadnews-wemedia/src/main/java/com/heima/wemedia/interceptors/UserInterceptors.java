package com.heima.wemedia.interceptors;

import com.heima.common.threadlocal.UserThreadLocalUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author Lenovo
 * @Date 2022/9/5 14:16
 * @Version 1.0
 */
@Slf4j
public class UserInterceptors implements HandlerInterceptor {
    /**
     * @Description: 从请求头获取userId放入当前线程Threadlocal中
     * @param request
     * @param response
     * @param handler
     * @return boolean
     * @Author wangzifeng
     * @CreateTime 2022/9/5 14:22
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //根据请求头获取用户Id
        String userId = request.getHeader("userId");
        //放入TheadLocal中
        if (StringUtils.isNotBlank(userId)) {
            UserThreadLocalUtils.setUserId(Integer.valueOf(userId));
            return true;
        }
        log.error("======无法获取用户id======");
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //ThreadLocal使用注意:防止内存泄露,也就是占用的内存没有被回收,多次内存泄漏可能会导致内存溢出
        //每次使用完ThreadLocal都要使用remove方法,清除存储数据
        UserThreadLocalUtils.removeUser();
    }
}
