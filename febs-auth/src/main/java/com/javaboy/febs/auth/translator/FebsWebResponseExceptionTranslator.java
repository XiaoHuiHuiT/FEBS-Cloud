package com.javaboy.febs.auth.translator;

import com.javaboy.febs.common.entity.FebsResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.UnsupportedGrantTypeException;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

/* @sl4j注解为lombok类型注解，用于往当前类中注入org.slf4j.Logger日志打印对象*/
@Slf4j
/* @Component注解用于将当前类注册为一个Bean*/
@Component
/* FebsWebResponseExceptionTranslator实现了WebResponseExceptionTranslator接口,用于覆盖默认的认证异常响应*/
public class FebsWebResponseExceptionTranslator implements WebResponseExceptionTranslator {

    /*
    在translate方法中，我们通过Exception异常对象的类型和内容将异常归类，并且统一返回500HTTP状态码（HttpStatus.INTERNAL_SERVER_ERROR）
    * */
    @Override
    public ResponseEntity translate(Exception e) {
        ResponseEntity.BodyBuilder status = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
        FebsResponse response = new FebsResponse();

        String message = "认证失败";

        log.error(message, e);

        if (e instanceof UnsupportedGrantTypeException) {
            message = "不支持该认证类型";
            return status.body(response.message(message));
        }
        if (e instanceof InvalidGrantException) {
            if (StringUtils.containsIgnoreCase(e.getMessage(), "Invalid refresh token")) {
                message = "refresh token无效";
                return status.body(response.message(message));
            }
            if (StringUtils.containsIgnoreCase(e.getMessage(), "locked")) {
                message = "用户已被锁定，请联系管理员";
                return status.body(response.message(message));
            }
            message = "用户名或密码错误";
            return status.body(response.message(message));
        }
        return status.body(response.message(message));
    }
}