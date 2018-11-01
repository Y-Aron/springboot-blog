package com.aron.blog.domain.mapper;

import com.aron.blog.domain.mapper.provider.PermissionSqlProvider;
import com.aron.blog.domain.entity.Permission;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author: Y-Aron
 * @create: 2018-10-12 14:48
 **/
public interface PermissionMapper {

    @Select("SELECT `url`, `http_method`, `name` FROM tb_auth_permission")
    List<Permission> findAll();

    @InsertProvider(type = PermissionSqlProvider.class, method = "insert")
    void  insert(Permission permission);
    //SELECT * FROM tb_auth_permission WHERE id IN
    //(SELECT permission_id FROM tb_auth_role_permission WHERE role_id=
    //(SELECT role_id FROM tb_auth_user_role WHERE user_id=1424874115706880)
    //)

    @Select("SELECT * FROM tb_auth_permission WHERE id IN" +
            "(SELECT permission_id FROM tb_auth_role_permission WHERE role_id=" +
            "(SELECT role_id FROM tb_auth_user_role WHERE user_id=#{userId}))")
    List<Permission> findByUserId(long userId);

}
