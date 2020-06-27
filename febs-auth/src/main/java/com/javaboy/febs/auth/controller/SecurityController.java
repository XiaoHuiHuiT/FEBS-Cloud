package com.javaboy.febs.auth.controller;

import com.javaboy.febs.auth.service.ValidateCodeService;
import com.javaboy.febs.common.entity.FebsResponse;
import com.javaboy.febs.common.exception.FebsAuthException;
import com.javaboy.febs.common.exception.ValidateCodeException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

@RestController
public class SecurityController {

    private final ConsumerTokenServices consumerTokenServices;
    private final ValidateCodeService validateCodeService;

    public SecurityController(ConsumerTokenServices consumerTokenServices, ValidateCodeService validateCodeService) {
        this.consumerTokenServices = consumerTokenServices;
        this.validateCodeService = validateCodeService;
    }

    @GetMapping("oauth/test")
    public String testOauth() {
        return "oauth";
    }

    @GetMapping("user")
    /* currentUser用户获取当前登录用户*/
    public Principal currentUser(Principal principal) {
        return principal;
    }

    @DeleteMapping("signout")
    /* signout方法通过ConsumerTokenServices来注销当前Token*/
    /* FebsResponse为系统的统一相应格式*/
    public FebsResponse signout(HttpServletRequest request) throws FebsAuthException {
        String authorization = request.getHeader("Authorization");
        String token = StringUtils.replace(authorization, "bearer ", "");
        FebsResponse febsResponse = new FebsResponse();
        if (!consumerTokenServices.revokeToken(token)) {
            throw new FebsAuthException("退出登录失败");
        }
        return febsResponse.message("退出登录成功");
    }

    /**
     * 生成验证码，
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws IOException           IO异常
     * @throws ValidateCodeException 验证码异常
     */
    @GetMapping("captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws IOException, ValidateCodeException {
        validateCodeService.create(request, response);
    }
}