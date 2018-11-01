package com.aron.blog.domain.entity;

import lombok.Data;
import lombok.ToString;

/**
 * @author: Y-Aron
 * @create: 2018-10-12 20:39
 **/
@Data
@ToString
public class RoleOrPermission {
    private int id;
    private String url;
    private String httpMethod;
    private String module;
    private String name;
//    private int permissionId;
//    private int roleId;
}
