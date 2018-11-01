package com.aron.blog.domain.entity;

import lombok.Data;
import lombok.ToString;

/**
 * @author: Y-Aron
 * @create: 2018-10-12 13:56
 **/
@Data
@ToString
public class Role {
    private int id;
    private String module;
    private String name;
    private int level;
    private String description;
}
