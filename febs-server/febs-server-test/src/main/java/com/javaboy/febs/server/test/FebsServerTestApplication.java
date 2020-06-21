package com.javaboy.febs.server.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/* 开启Spring Cloud Security权限注解*/
@EnableGlobalMethodSecurity(prePostEnabled = true)
/* 开启服务注册与发现*/
@EnableDiscoveryClient
@SpringBootApplication
public class FebsServerTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(FebsServerTestApplication.class, args);
    }

}
