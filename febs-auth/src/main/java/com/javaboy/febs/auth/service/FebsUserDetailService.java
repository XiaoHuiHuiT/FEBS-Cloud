package com.javaboy.febs.auth.service;

import com.javaboy.febs.common.entity.FebsAuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
/* FebsUserDetailService实现了UserDetailsService接口的loadUserByUsername方法*/
public class FebsUserDetailService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    /*
    loadUserByUsername方法返回一个UserDetails对象，该对象也是一个接口，包含一些用于描述用户信息的方法
    getAuthorities获取用户包含的权限，返回权限集合，权限是一个继承了GrantedAuthority的对象；

    getPassword和getUsername用于获取密码和用户名；
    isAccountNonExpired方法返回boolean类型，用于判断账户是否未过期，未过期返回true反之返回false；
    isAccountNonLocked方法用于判断账户是否未锁定；
    isCredentialsNonExpired用于判断用户凭证是否没过期，即密码是否未过期；
    isEnabled方法用于判断用户是否可用。
    * */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        /* 模拟了一个用户，用户名为用户输入的用户名，密码为123456（后期再改造为从数据库中获取用户）*/
        FebsAuthUser user = new FebsAuthUser();
        user.setUsername(username);
        user.setPassword(this.passwordEncoder.encode("123456"));

        /* 这里使用的是User类包含7个参数的构造器，其还包含一个三个参数的构造器*/
        return new User(username, user.getPassword(), user.isEnabled(),
                user.isAccountNonExpired(), user.isCredentialsNonExpired(),
                /* 由于权限参数不能为空，所以这里先使用AuthorityUtils.commaSeparatedStringToAuthorityList方法模拟一个user:add权限*/
                user.isAccountNonLocked(), AuthorityUtils.commaSeparatedStringToAuthorityList("user:add"));
    }
}