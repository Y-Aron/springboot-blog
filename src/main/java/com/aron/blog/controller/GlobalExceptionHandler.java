package com.aron.blog.controller;

import com.alibaba.fastjson.JSON;
import com.aron.blog.domain.entity.GlobalExecption;
import com.aron.blog.domain.entity.ResultBean;
import com.aron.blog.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: Y-Aron
 * @create: 2018-10-11 00:29
 **/
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public GlobalExceptionHandler(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @ExceptionHandler(value = Exception.class)
    public ResultBean defaultErrorHandler(HttpServletRequest request, Exception e){
        e.printStackTrace();
        // 将错误记录写入mongoDB
        saveToMongoDB(request, e);

        ResultBean resultBean = new ResultBean();
        if (e instanceof NoHandlerFoundException) {
            resultBean.setCode(404);
            resultBean.setMessage("请求URL不存在~");
        } else {
            resultBean.setCode(500);
            resultBean.setMessage("哎呀~ 系统发生了错误 ...");
        }
        resultBean.setStatus(false);
        return resultBean;
    }

    private void saveToMongoDB(HttpServletRequest request, Exception e){
        // 初始化错误消息对象 ...
        GlobalExecption globalExecption = new GlobalExecption();
        globalExecption.setRequestTime(System.currentTimeMillis());
        globalExecption.setUrl(request.getRequestURL().toString());
        globalExecption.setClassMethod(request.getServletPath());
        globalExecption.setHttpMethod(request.getMethod());
        globalExecption.setIp(IpUtils.getIpAddress(request));
        globalExecption.setRequestArgs(JSON.toJSONString(request.getParameterMap()));
        globalExecption.setErrorMessage(e.getMessage());
        // 打印在控制台
        log.error("错误消息：{}", globalExecption.getErrorMessage());
        // 写入mongodb
        mongoTemplate.save(globalExecption);
    }
}
