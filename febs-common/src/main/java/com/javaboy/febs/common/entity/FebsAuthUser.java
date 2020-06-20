package com.javaboy.febs.common.entity;

import lombok.Data;

import java.io.Serializable;

/* 注解为lombok注解，用于自动生成get，set方法*/
@Data
public class FebsAuthUser implements Serializable {
    private static final long serialVersionUID = -1748289340320186418L;

    private String username;

    private String password;

    private boolean accountNonExpired = true;

    private boolean accountNonLocked = true;

    private boolean credentialsNonExpired = true;

    private boolean enabled = true;
}