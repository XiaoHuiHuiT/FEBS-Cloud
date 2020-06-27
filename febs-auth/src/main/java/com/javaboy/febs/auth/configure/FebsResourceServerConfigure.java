package com.javaboy.febs.auth.configure;

import com.javaboy.febs.auth.properties.FebsAuthProperties;
import com.javaboy.febs.common.handler.FebsAccessDeniedHandler;
import com.javaboy.febs.common.handler.FebsAuthExceptionEntryPoint;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
/* @EnableResourceServer用于开启资源服务器相关配置*/
@EnableResourceServer
/*  FebsResourceServerConfigure继承了ResourceServerConfigurerAdapter
 *  FebsResourceServerConfigure用于处理非/oauth/开头的请求，其主要用于资源的保护，客户端只能通过OAuth2协议发放的令牌来从资源服务器中获取受保护的资源
 * */
/**
 * @author jonath_yh
 */
public class FebsResourceServerConfigure extends ResourceServerConfigurerAdapter {

    private final FebsAccessDeniedHandler accessDeniedHandler;
    private final FebsAuthExceptionEntryPoint exceptionEntryPoint;
    private final FebsAuthProperties properties;

    public FebsResourceServerConfigure(FebsAccessDeniedHandler accessDeniedHandler, FebsAuthExceptionEntryPoint exceptionEntryPoint, FebsAuthProperties properties) {
        this.accessDeniedHandler = accessDeniedHandler;
        this.exceptionEntryPoint = exceptionEntryPoint;
        this.properties = properties;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.authenticationEntryPoint(exceptionEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);
    }

    /* 重写了configure(HttpSecurity http)方法*/

    /**
     * configure(HttpSecurity http)方法里，
     * 我们通过.antMatchers(anonUrls).permitAll()配置了免认证资源，anonUrls为免认证资源数组，
     * 是从FebsAuthProperties配置中读取出来的值经过逗号分隔后的结果
     *
     * @param http HttpSecurity
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        String[] anonUrls = StringUtils.splitByWholeSeparatorPreserveAllTokens(properties.getAnonUrl(), ",");
        http.csrf().disable()
                /* requestMatchers().antMatchers("/**")的配置表明该安全配置对所有请求都生效*/
                .requestMatchers().antMatchers("/**")
                .and()
                .authorizeRequests()
                .antMatchers(anonUrls).permitAll()
                .antMatchers("/**").authenticated()
                .and().httpBasic();
    }
}