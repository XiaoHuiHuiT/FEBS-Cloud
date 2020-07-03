package com.javaboy.febs.server.system;

import com.javaboy.febs.common.annotation.EnableFebsAuthExceptionHandler;
import com.javaboy.febs.common.annotation.FebsCloudApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableFebsAuthExceptionHandler
/* 开启Spring Cloud Security权限注解*/
@EnableGlobalMethodSecurity(prePostEnabled = true)
/* 开启服务注册与发现*/
@EnableDiscoveryClient
@FebsCloudApplication
// 让该Mapper注册到IOC容器中
@MapperScan("com.javaboy.febs.server.system.mapper")
// 开启数据库事务
@EnableTransactionManagement
@SpringBootApplication
public class FebsServerSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(FebsServerSystemApplication.class, args);
    }

}