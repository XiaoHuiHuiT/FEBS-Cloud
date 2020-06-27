package com.javaboy.febs.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.javaboy.febs.common.entity.system.SystemUser;

public interface UserMapper extends BaseMapper<SystemUser> {
    /**
     * findByName方法用于通过用户名查找用户信息
     * @param username 用户名
     * @return SystemUser
     */
    SystemUser findByName(String username);
}