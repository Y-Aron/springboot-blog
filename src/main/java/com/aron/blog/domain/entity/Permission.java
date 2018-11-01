package com.aron.blog.domain.entity;

import lombok.Data;
import lombok.ToString;

/**
 * @author: Y-Aron
 * @create: 2018-10-12 14:25
 **/
@Data
@ToString
public class Permission {
    private int id;
    private String url;
    private String httpMethod;
    private String name;
    private String description;
}
