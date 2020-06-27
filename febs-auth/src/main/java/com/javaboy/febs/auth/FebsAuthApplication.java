package com.javaboy.febs.auth;

import com.javaboy.febs.common.annotation.EnableFebsLettuceRedis;
import com.javaboy.febs.common.annotation.FebsCloudApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

// 使用Lettuce Redis
@EnableFebsLettuceRedis
@SpringBootApplication
@FebsCloudApplication
/* 开启服务注册与发现功能*/
@EnableDiscoveryClient
// 将路径下的Mapper类都注册到IOC容器中
@MapperScan("com.javaboy.febs.auth.mapper")
public class FebsAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(FebsAuthApplication.class, args);
    }

}