package com.aron.blog.service.impl;

import com.aron.blog.domain.mapper.RoleMapper;
import com.aron.blog.domain.entity.Role;
import com.aron.blog.service.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: Y-Aron
 * @create: 2018-10-12 21:11
 **/
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleMapper roleMapper;

    @Autowired
    public RoleServiceImpl(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    @Override
    public List<Role> findRolesByUserId(long userId) {
        if (userId > 0) {
            return roleMapper.findRoleByUser(userId);
        }
        return null;
    }

    @Override
    public List<Role> findRoleByPermission(String httpMethod, String url) {
        if (StringUtils.isBlank(httpMethod) || StringUtils.isBlank(url)) {
            return null;
        }
        return roleMapper.findRoleByPermission(httpMethod, url);
    }
}
