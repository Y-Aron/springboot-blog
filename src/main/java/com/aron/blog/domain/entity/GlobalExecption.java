package com.aron.blog.domain.entity;

import lombok.Data;
import lombok.ToString;

/**
 * @author: Y-Aron
 * @create: 2018-10-11 14:06
 **/
@Data
@ToString
public class GlobalExecption {
    private String url;
    private String httpMethod;
    private String ip;
    private String classMethod;
    private String requestArgs;
    private long requestTime;
    private String errorMessage;
}
