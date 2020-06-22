package com.javaboy.febs.auth.configure;

import com.javaboy.febs.auth.properties.FebsAuthProperties;
import com.javaboy.febs.auth.properties.FebsClientsProperties;
import com.javaboy.febs.auth.service.FebsUserDetailService;
import com.javaboy.febs.auth.translator.FebsWebResponseExceptionTranslator;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * 认证服务器相关的安全配置类
 */
@Configuration
/* @EnableAuthorizationServer开启认证服务器相关配置*/
@EnableAuthorizationServer
/* FebsAuthorizationServerConfigure继承AuthorizationServerConfigurerAdapter适配器*/
public class FebsAuthorizationServerConfigure extends AuthorizationServerConfigurerAdapter {

    /* 注入了在FebsSecurityConfigure配置类中注册的BeanAuthenticationManager和PasswordEncoder*/
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
    @Autowired
    private FebsUserDetailService userDetailService;

    @Autowired
    private FebsAuthProperties authProperties;

    @Autowired
    private FebsWebResponseExceptionTranslator exceptionTranslator;

    /*
    1.客户端从认证服务器获取令牌的时候，必须使用client_id为febs，client_secret为123456的标识来获取
    2.该client_id支持password模式获取令牌，并且可以通过refresh_token来获取新的令牌
    3.在获取client_id为febs的令牌的时候，scope只能指定为all，否则将获取失败

    configure(ClientDetailsServiceConfigurer clients)方法由原先硬编码的形式改造成了从配置文件读取配置的形式，并且判断了client和secret不能为空
    **/
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        FebsClientsProperties[] clientsArray = authProperties.getClients();
        InMemoryClientDetailsServiceBuilder builder = clients.inMemory();
        if (ArrayUtils.isNotEmpty(clientsArray)) {
            for (FebsClientsProperties client : clientsArray) {
                if (StringUtils.isBlank(client.getClient())) {
                    throw new Exception("client不能为空");
                }
                if (StringUtils.isBlank(client.getSecret())) {
                    throw new Exception("secret不能为空");
                }
                String[] grantTypes = StringUtils.splitByWholeSeparatorPreserveAllTokens(client.getGrantType(), ",");
                builder.withClient(client.getClient())
                        .secret(passwordEncoder.encode(client.getSecret()))
                        .authorizedGrantTypes(grantTypes)
                        .scopes(client.getScope());
            }
        }
    }

    @Override
    @SuppressWarnings("all")
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        /* tokenStore使用的是RedisTokenStore，认证服务器生成的令牌将被存储到Redis中*/
        endpoints.tokenStore(tokenStore())
                .userDetailsService(userDetailService)
                .authenticationManager(authenticationManager)
                .tokenServices(defaultTokenServices())
                .exceptionTranslator(exceptionTranslator);
    }

    @Bean
    public TokenStore tokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }

    @Primary
    @Bean
    /* 指定了令牌的基本配置，比如令牌有效时间为60 * 60 * 24秒，刷新令牌有效时间为60 * 60 * 24 * 7秒
     *  defaultTokenServices方法指定有效时间也从原先硬编码的形式改造成了从配置文件读取配置的形式。
     * */
    public DefaultTokenServices defaultTokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(tokenStore());

        /* setSupportRefreshToken设置为true表示开启刷新令牌的支持*/
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setAccessTokenValiditySeconds(authProperties.getAccessTokenValiditySeconds());
        tokenServices.setRefreshTokenValiditySeconds(authProperties.getRefreshTokenValiditySeconds());
        return tokenServices;
    }
}