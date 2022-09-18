package com.heima.wemedia.client;

import com.heima.model.media.dtos.WmUserDto;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.PrimitiveIterator;

/**
 * @Author Lenovo
 * @Date 2022/9/4 12:38
 * @Version 1.0
 */
@FeignClient("leadnews-wemedia")
public interface WemediaFeign {
    /**
     * @Description: 保存自媒体用户,注意这里
     *               与自媒体对应controller里的方法的路径,请求方式,请求参数,返回类型要一致!!!!!
     * @param wmUserDto
     * @return Integer
     * @Author wangzifeng
     * @CreateTime 2022/9/4 12:24
     */
    @PostMapping("/api/v1/wmuser/save")
    Integer saveWmUser(@RequestBody WmUserDto wmUserDto);

}
