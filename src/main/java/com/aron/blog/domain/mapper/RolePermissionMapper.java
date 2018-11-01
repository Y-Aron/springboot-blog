package com.aron.blog.domain.mapper;

import com.aron.blog.domain.entity.RoleOrPermission;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author: Y-Aron
 * @create: 2018-10-14 02:55
 **/
public interface RolePermissionMapper {

    @Select("SELECT rp.id, p.url, p.http_method, r.module, r.`name` FROM tb_auth_role_permission AS rp " +
            "LEFT JOIN tb_auth_permission AS p ON rp.permission_id=p.id " +
            "LEFT JOIN tb_auth_role AS r ON rp.role_id=r.id")
    List<RoleOrPermission> findAll();

}
