package com.javaboy.febs.auth.configure;

import com.javaboy.febs.auth.service.FebsUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

/* 该类继承了WebSecurityConfigurerAdapter适配器，重写了几个方法*/
/* 开启了和Web相关的安全配置*/
@EnableWebSecurity
/* 以/oauth/开头的请求由FebsSecurityConfigure过滤器链处理，剩下的其他请求由FebsResourceServerConfigure过滤器链处理,当前过滤器链执行的权限比较优先执行*/
@Order(2)
/* FebsSecurityConfigure用于处理和令牌相关的请求*/
public class FebsSecurityConfigure extends WebSecurityConfigurerAdapter {

    /* 注入了FebsUserDetailService*/
    private final FebsUserDetailService userDetailService;
    private final PasswordEncoder passwordEncoder;

    public FebsSecurityConfigure(FebsUserDetailService userDetailService, PasswordEncoder passwordEncoder) {
        this.userDetailService = userDetailService;
        this.passwordEncoder = passwordEncoder;
    }

    /* PasswordEncoder类型的Bean，该类是一个接口，定义了几个和密码加密校验相关的方法，这里我们使用的是Spring Security内部实现好的BCryptPasswordEncoder
     *  BCryptPasswordEncoder的特点就是，对于一个相同的密码，每次加密出来的加密串都不同
     * */
    /*@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }*/

    /* 注册了一个authenticationManagerBean，因为密码模式需要使用到这个Bean*/
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /* 重写了WebSecurityConfigurerAdapter类的configure(HttpSecurity http)方法*/
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /* requestMatchers().antMatchers("/oauth/**")的含义是：FebsSecurityConfigure安全配置类只对/oauth/开头的请求有效*/
        http.requestMatchers()
                .antMatchers("/oauth/**")
                .and()
                .authorizeRequests()
                .antMatchers("/oauth/**").authenticated()
                .and()
                .csrf().disable();
    }

    /* 重写了configure(AuthenticationManagerBuilder auth)方法*/
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /* 指定了userDetailsService和passwordEncoder*/
        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder);
    }
}