package com.javaboy.febs.server.system.configure;

import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.parsers.BlockAttackSqlParser;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.javaboy.febs.server.system.properties.FebsServerSystemProperties;
import com.javaboy.febs.server.system.properties.FebsSwaggerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/*
 * MyBatis Plus分页插件配置
 * */
@Configuration
// @EnableSwagger2标注，表示开启Swagger功能
@EnableSwagger2
public class FebsWebConfigure {

    @Autowired
    private FebsServerSystemProperties properties;

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        List<ISqlParser> sqlParserList = new ArrayList<>();
        sqlParserList.add(new BlockAttackSqlParser());
        paginationInterceptor.setSqlParserList(sqlParserList);
        return paginationInterceptor;
    }

    /**
     * swaggerApi方法的apis(RequestHandlerSelectors.basePackage("com.javaboy.febs.server.system.controller"))
     * 表示将com.javaboy.febs.server.system.controller路径下的所有Controller都添加进去
     * <p>
     * paths(PathSelectors.any())表示Controller里的所有方法都纳入
     *
     * @return Docket
     */
    @Bean
    public Docket swaggerApi() {
        FebsSwaggerProperties swagger = properties.getSwagger();
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(swagger.getBasePackage()))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo(swagger))
                .securitySchemes(Collections.singletonList(securityScheme(swagger)))
                .securityContexts(Collections.singletonList(securityContext(swagger)));
    }

    /**
     * apiInfo用于定义一些API页面信息，比如作者名称，邮箱，网站链接，开源协议等等
     *
     * @return ApiInfo
     */
    private ApiInfo apiInfo(FebsSwaggerProperties swagger) {
        return new ApiInfo(
                swagger.getTitle(),
                swagger.getDescription(),
                swagger.getVersion(),
                null,
                new Contact(swagger.getAuthor(), swagger.getUrl(), swagger.getEmail()),
                swagger.getLicense(), swagger.getLicenseUrl(), Collections.emptyList());
    }

    /**
     * securitySchemes：用于配置安全策略，比如配置认证模型，scope等内容
     * securityScheme方法中，我们通过OAuthBuilder对象构建了安全策略，
     * 主要配置了认证类型为ResourceOwnerPasswordCredentialsGrant（即密码模式），
     * 认证地址为http://localhost:8301/auth/oauth/token（即通过网关转发到认证服务器），
     * scope为test，和febs-auth模块里定义的一致。这个安全策略我们将其命名为febs_oauth_swagger
     *
     * @param swagger
     * @return SecurityScheme
     */
    private SecurityScheme securityScheme(FebsSwaggerProperties swagger) {
        GrantType grantType = new ResourceOwnerPasswordCredentialsGrant(swagger.getGrantUrl());

        return new OAuthBuilder()
                .name(swagger.getName())
                .grantTypes(Collections.singletonList(grantType))
                .scopes(Arrays.asList(scopes(swagger)))
                .build();
    }


    /**
     * securityContexts：用于配置安全上下文，只有配置了安全上下文的接口才能使用令牌获取资源
     * securityContext方法中，我们通过febs_oauth_swagger名称关联了上面定义的安全策略，
     * 并且通过forPaths(PathSelectors.any())设置所有API接口都用这个安全上下文
     *
     * @param swagger
     * @return SecurityContext
     */
    private SecurityContext securityContext(FebsSwaggerProperties swagger) {
        return SecurityContext.builder()
                .securityReferences(Collections.singletonList(new SecurityReference(swagger.getName(), scopes(swagger))))
                .forPaths(PathSelectors.any())
                .build();
    }

    private AuthorizationScope[] scopes(FebsSwaggerProperties swagger) {
        return new AuthorizationScope[]{
                new AuthorizationScope(swagger.getScope(), "")
        };
    }
}