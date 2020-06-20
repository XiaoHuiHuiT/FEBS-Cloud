package com.javaboy.febs.auth.configure;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
/* @EnableResourceServer用于开启资源服务器相关配置*/
@EnableResourceServer
/* FebsResourceServerConfigure继承了ResourceServerConfigurerAdapter
*  FebsResourceServerConfigure用于处理非/oauth/开头的请求，其主要用于资源的保护，客户端只能通过OAuth2协议发放的令牌来从资源服务器中获取受保护的资源
* */
public class FebsResourceServerConfigure extends ResourceServerConfigurerAdapter {

    /* 重写了configure(HttpSecurity http)方法*/
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                /* requestMatchers().antMatchers("/**")的配置表明该安全配置对所有请求都生效*/
                .requestMatchers().antMatchers("/**")
                .and()
                .authorizeRequests()
                .antMatchers("/**").authenticated();
    }
}