package com.heima.admin.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Lenovo
 * @Date 2022/9/1 17:52
 * @Version 1.0
 */
@ComponentScan(basePackages = {"com.heima.common.exception","com.heima.common.knife4j","com.heima.common.bcrypt"})
@Configuration
public class InitConfig {

}
