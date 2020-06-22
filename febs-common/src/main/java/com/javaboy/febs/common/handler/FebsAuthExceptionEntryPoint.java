package com.javaboy.febs.common.handler;

import com.javaboy.febs.common.entity.FebsResponse;
import com.javaboy.febs.common.utils.FebsUtil;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/* FebsAuthExceptionEntryPoint实现了AuthenticationEntryPoint接口的commence方法，在方法内自定义了响应的格式*/
public class FebsAuthExceptionEntryPoint implements AuthenticationEntryPoint {

    /* 其中application/json;charset=UTF-8和HTTP状态码401，Spring都提供了相应的常量类*/
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        FebsResponse febsResponse = new FebsResponse();
        FebsUtil.makeResponse(
                response, MediaType.APPLICATION_JSON_UTF8_VALUE,
                HttpServletResponse.SC_UNAUTHORIZED, febsResponse.message("token无效")
        );
    }
}