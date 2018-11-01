package com.aron.blog.domain.mapper.provider;

import com.aron.blog.domain.entity.Permission;

/**
 * @author: Y-Aron
 * @create: 2018-10-12 17:56
 **/
public class PermissionSqlProvider {

    private static final String TABLE_NAME = "tb_auth_permission";

    public String insert(Permission permission){
        StringBuilder sql = new StringBuilder("INSERT INTO ")
                .append(TABLE_NAME)
                .append(" SET `url`=#{url}, `http_method`=#{httpMethod}");
        if (permission.getName() != null){
            sql.append(", `name`=#{name}");
        }
        if (permission.getDescription() != null) {
            sql.append(", `description`=#{description}");
        }
        return sql.toString();
    }
}
