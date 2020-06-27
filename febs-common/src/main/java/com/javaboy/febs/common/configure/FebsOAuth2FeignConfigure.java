package com.javaboy.febs.common.configure;

import com.javaboy.febs.common.entity.FebsConstant;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.util.Base64Utils;

public class FebsOAuth2FeignConfigure {

    /**
     * 注册一个Feign请求拦截器
     * 因为现在微服务需要校验Zuul Token，所以我们需要在上一节定义的Feign拦截器里也加入Zuul Token，否则Feign调用微服务会报403异常
     */
    @Bean
    public RequestInterceptor oauth2FeignRequestInterceptor() {
        return requestTemplate -> {
            // 添加 Zuul Token
            String zuulToken = new String(Base64Utils.encode(FebsConstant.ZUUL_TOKEN_VALUE.getBytes()));
            requestTemplate.header(FebsConstant.ZUUL_TOKEN_HEADER, zuulToken);

            /* 通过SecurityContextHolder从请求上下文中获取了OAuth2AuthenticationDetails类型对象，并通过该对象获取到了请求令牌
            然后在请求模板对象requestTemplate的头部手动将令牌添加上去*/
            Object details = SecurityContextHolder.getContext().getAuthentication().getDetails();
            if (details instanceof OAuth2AuthenticationDetails) {
                String authorizationToken = ((OAuth2AuthenticationDetails) details).getTokenValue();
                requestTemplate.header(HttpHeaders.AUTHORIZATION, "bearer " + authorizationToken);
            }
        };
    }
}