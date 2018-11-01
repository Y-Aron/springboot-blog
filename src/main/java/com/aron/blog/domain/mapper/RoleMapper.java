package com.aron.blog.domain.mapper;

import com.aron.blog.domain.entity.Role;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RoleMapper {

    @Select("SELECT `id`, `module`, `name` FROM tb_auth_role WHERE id IN" +
            "(SELECT role_id FROM tb_auth_user_role WHERE user_id=#{userId})")
    List<Role> findRoleByUser(long userId);

    @Select("SELECT `id` FROM tb_auth_role WHERE id IN" +
            "(SELECT role_id FROM tb_auth_role_permission AS rp WHERE permission_id =" +
            "(SELECT id FROM tb_auth_permission WHERE http_method=#{httpMethod} AND  url=#{url} ))")
    List<Role> findRoleByPermission(@Param("httpMethod") String httpMethod,
                                    @Param("url") String url);
}