package com.javaboy.febs.auth.manager;

import com.javaboy.febs.auth.mapper.MenuMapper;
import com.javaboy.febs.auth.mapper.UserMapper;
import com.javaboy.febs.common.entity.system.Menu;
import com.javaboy.febs.common.entity.system.SystemUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 统一定义和用户相关的业务方法
 */
@Service
public class UserManager {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MenuMapper menuMapper;

    /**
     * 根据用户名查询用户详细信息
     * @param username 用户名
     * @return SystemUser用户实体
     */
    public SystemUser findByName(String username) {
        return userMapper.findByName(username);
    }

    /**
     * 根据用户名查询该用户的权限
     * @param username 用户名
     * @return String "user:view,user:add"
     */
    public String findUserPermissions(String username) {
        // Java 8的Stream简化
        List<Menu> userPermissions = menuMapper.findUserPermissions(username);
        return userPermissions.stream().map(Menu::getPerms).collect(Collectors.joining(","));
    }
}