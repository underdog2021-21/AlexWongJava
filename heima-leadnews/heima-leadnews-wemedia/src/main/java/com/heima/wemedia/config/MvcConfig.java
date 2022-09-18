package com.heima.wemedia.config;

import com.heima.wemedia.interceptors.UserInterceptors;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author Lenovo
 * @Date 2022/9/5 15:28
 * @Version 1.0
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    /**
     * 注册拦截器
     * 排除 登录请求 和 远程调用
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserInterceptors())
                //把所有路径添加拦截器
                .addPathPatterns("/**")
                //这些路径不会添加拦截器
                .excludePathPatterns("/login/in","/api/v1/wmuser/save");
    }
}

