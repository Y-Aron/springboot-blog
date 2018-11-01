package com.aron.blog.domain.entity;

import lombok.Data;

/**
 * @author: Y-Aron
 * @create: 2018-10-11 00:49
 **/
@Data
public class WebLog {
    private String url;
    private String httpMethod;
    private String ip;
    private String classMethod;
    private String requestArgs;
    private String respData;
    private long requestTime;
    private String processingTime;
}
