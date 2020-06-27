package com.javaboy.febs.common.configure;

import com.javaboy.febs.common.interceptor.FebsServerProtectInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class FebsServerProtectConfigure implements WebMvcConfigurer {

    /**
     * 因为各个微服务系统里可以自定义自个儿的PasswordEncoder，
     * 所以这里使用了@ConditionalOnMissingBean(value = PasswordEncoder.class)注解标注，
     * 表示当IOC容器里没有PasswordEncoder类型的Bean的时候，则将BCryptPasswordEncoder注册到IOC容器中
     * @return PasswordEncoder
     */
    @Bean
    @ConditionalOnMissingBean(value = PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 注册了FebsServerProtectInterceptor
     *
     * @return HandlerInterceptor
     */
    @Bean
    public HandlerInterceptor febsServerProtectInterceptor() {
        return new FebsServerProtectInterceptor();
    }

    /**
     * 并且将它加入到了Spring的拦截器链中
     *
     * @param registry 拦截器注册
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(febsServerProtectInterceptor());
    }
}
