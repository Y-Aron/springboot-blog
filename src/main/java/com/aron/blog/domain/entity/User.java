package com.aron.blog.domain.entity;

import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

/**
 * @author Y-Aron
 * @create 2018-10-08 22:10
 */
@Data
@ToString
public class User {
    private long id;
    private String username;
    private String password;
    private Timestamp createTime;
    private Timestamp updateTime;
}