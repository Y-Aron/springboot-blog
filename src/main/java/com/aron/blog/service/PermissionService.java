package com.aron.blog.service;

import com.aron.blog.domain.entity.Permission;

import java.util.List;

public interface PermissionService {
    /**
     * 获取权限列表
     * @return
     */
    List<Permission> getPermissionList();


    /**
     * 创建权限数据
     * @param p
     */
    void createPermission(Permission p);

    List<Permission> findByUserId(long userId);

}
