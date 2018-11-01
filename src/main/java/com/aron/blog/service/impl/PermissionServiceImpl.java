package com.aron.blog.service.impl;

import com.aron.blog.domain.mapper.PermissionMapper;
import com.aron.blog.domain.entity.Permission;
import com.aron.blog.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: Y-Aron
 * @create: 2018-10-12 14:56
 **/
@Service
public class PermissionServiceImpl implements PermissionService {

    private final PermissionMapper permissionMapper;

    @Autowired
    public PermissionServiceImpl(PermissionMapper permissionMapper) {
        this.permissionMapper = permissionMapper;
    }

    @Override
    public List<Permission> getPermissionList() {
        return permissionMapper.findAll();
    }

    @Override
    public void createPermission(Permission p) {
        if (p != null) {
            permissionMapper.insert(p);
        }
    }

    @Override
    public List<Permission> findByUserId(long userId) {
        return permissionMapper.findByUserId(userId);
    }
}
