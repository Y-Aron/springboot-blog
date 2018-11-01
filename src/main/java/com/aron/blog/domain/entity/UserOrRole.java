package com.aron.blog.domain.entity;

import lombok.Data;
import lombok.ToString;

/**
 * @author: Y-Aron
 * @create: 2018-10-12 20:40
 **/
@Data
@ToString
public class UserOrRole {
    private int id;
    private int roleId;
    private long userId;
}
