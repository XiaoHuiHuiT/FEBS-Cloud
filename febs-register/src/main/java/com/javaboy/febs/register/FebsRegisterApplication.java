package com.javaboy.febs.register;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
/* 开启Eureka服务端功能*/
@EnableEurekaServer
public class FebsRegisterApplication {

    public static void main(String[] args) {
        SpringApplication.run(FebsRegisterApplication.class, args);
    }

}