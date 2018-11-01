package com.aron.blog.domain.entity;

import lombok.Data;
import lombok.ToString;

/**
 * @author: Y-Aron
 * @create: 2018-10-11 17:57
 **/
@Data
@ToString
public class ResultBean {

    private boolean status;
    private int code;
    private String message;
    private Object data;
}
