package com.javaboy.febs.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;


/* @EnableZuulProxy开启Zuul服务网关功能*/
@EnableZuulProxy
/* @EnableDiscoveryClient开启服务注册与发现*/
@EnableDiscoveryClient
@SpringBootApplication
public class FebsGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(FebsGatewayApplication.class, args);
    }

}