package com.heima.admin.gateway.filter;

import com.alibaba.nacos.api.naming.pojo.AbstractHealthChecker;
import com.heima.common.dtos.Payload;
import com.heima.common.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @Author Lenovo
 * @Date 2022/9/2 19:40
 * @Version 1.0
 */
@Slf4j
@Component
public class AuthFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //通过交换机获得请求对象,因为要根据请求路径判断是否是登录请求,
        //所以要获得请求路径,所以要获取请求对象
        ServerHttpRequest req = exchange.getRequest();
        //通过交换机获得响应对象
        ServerHttpResponse resp = exchange.getResponse();
        //判断是否是登录请求
        if (req.getURI().getPath().contains("/login/in")) {
            log.info("是登录请求,放行");
            return chain.filter(exchange);
        }
        //如果不是登录请求,因为如果不是登录请求要看有没有token,
        // 没有的话,说明尚未登录,不能进行后续请求,
        // 要让该请求返回界面去登录
        // 如果有正确的token,说明已登录了
        // 从请求头获取token,获取userId,放在请求头里,
        //方便后面微服务不用每次都解析token
        String token = req.getHeaders().getFirst("token");
        //token为空,尚未登录,返回登录页面
        if (StringUtils.isBlank(token)) {
            log.info("token为空,401直接返回登录界面");
            //它向responseheader里set了Location
            //然后return了response.setComplete();
            //而这个方法正好返回一个Mono<void>
            //可以使用这个方法实现页面的重定向。
            resp.setComplete();
        }
        try {
            //token不为空,解析一下
            Payload infoFromToken = JwtUtils.getInfoFromToken(token);
            //获取userID
            Integer userId = infoFromToken.getUserId();
            //把userId放在请求头中,生成一个新的ServerHttpRequest,
            //后面的微服务就不需要再解析token了
            //mutate()和build()的作用就是改变该ServerHttpRequest
            ServerHttpRequest httpRequest = req.mutate().headers(httpHeaders -> {
                httpHeaders.add("userId", userId.toString());
            }).build();
            //把exchange中的ServerHttpRequest也改变
            exchange.mutate().request(httpRequest).build();
        } catch (Exception e) {
            log.error("token验证无效,返回401");
            resp.setStatusCode(HttpStatus.UNAUTHORIZED);
            return resp.setComplete();
        }


        log.info("全局过滤器验证jwt,通过,放行");
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
