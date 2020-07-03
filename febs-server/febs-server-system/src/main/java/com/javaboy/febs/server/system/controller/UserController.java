package com.javaboy.febs.server.system.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.javaboy.febs.common.entity.FebsResponse;
import com.javaboy.febs.common.entity.QueryRequest;
import com.javaboy.febs.common.entity.system.SystemUser;
import com.javaboy.febs.common.exception.FebsException;
import com.javaboy.febs.common.utils.FebsUtil;
import com.javaboy.febs.server.system.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Map;

@Slf4j
// @Validated的作用是让@NotBlank注解生效
@Validated
@RestController
/**
 * UserController的各个方法中，我们还使用了@PreAuthorize("hasAnyAuthority('xxx')")注解对方法进行了权限控制
 * UserController方法中，我们使用了@Validated和@Valid注解对请求参数进行了校验
 * @Valid对应实体对象传参时的参数校验
 * @Validated对应普通参数的校验
 */
@RequestMapping("user")
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('user:view')")
    public FebsResponse userList(QueryRequest queryRequest, SystemUser user) {
        Map<String, Object> dataTable = FebsUtil.getDataTable(userService.findUserDetail(user, queryRequest));
        return new FebsResponse().data(dataTable);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('user:add')")
    public void addUser(@Valid SystemUser user) throws FebsException {
        try {
            this.userService.createUser(user);
        } catch (Exception e) {
            String message = "新增用户失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('user:update')")
    public void updateUser(@Valid SystemUser user) throws FebsException {
        try {
            this.userService.updateUser(user);
        } catch (Exception e) {
            String message = "修改用户失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @DeleteMapping("/{userIds}")
    @PreAuthorize("hasAnyAuthority('user:delete')")
    // @NotBlank(message = "{required}")注解标注了userIds参数（更多可用的注解可以参考Spring表单校验），该注解表示请求参数不能为空，提示信息为{required}占位符里的内容
    public void deleteUsers(@NotBlank(message = "{required}") @PathVariable String userIds) throws FebsException {
        try {
            String[] ids = userIds.split(StringPool.COMMA);
            this.userService.deleteUsers(ids);
        } catch (Exception e) {
            String message = "删除用户失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
}