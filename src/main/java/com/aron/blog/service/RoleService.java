package com.aron.blog.service;

import com.aron.blog.domain.entity.Role;

import java.util.List;

/**
 * @author: Y-Aron
 * @create: 2018-10-12 21:09
 **/
public interface RoleService {

    List<Role> findRolesByUserId(long userId);

    List<Role> findRoleByPermission(String httpMethod, String url);
}
