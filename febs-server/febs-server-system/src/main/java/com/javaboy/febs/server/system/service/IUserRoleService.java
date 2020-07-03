package com.javaboy.febs.server.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.javaboy.febs.common.entity.system.UserRole;

/**
 * IUserRoleService为t_user_role数据表对应的service
 */
public interface IUserRoleService extends IService<UserRole> {

	void deleteUserRolesByRoleId(String[] roleIds);

	void deleteUserRolesByUserId(String[] userIds);
}