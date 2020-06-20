package com.javaboy.febs.register.configure;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/* 该配置类用于开启Eureka服务端端点保护*/
@EnableWebSecurity
public class FebsRegisterWebSecurityConfigure extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /* 忽略/eureka/**的Http请求，别的都需要认证*/
        http.csrf().ignoringAntMatchers("/eureka/**");
        super.configure(http);
    }
}