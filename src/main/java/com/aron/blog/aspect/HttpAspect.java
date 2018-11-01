package com.aron.blog.aspect;

import com.alibaba.fastjson.JSON;
import com.aron.blog.domain.entity.WebLog;
import com.aron.blog.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: Y-Aron
 * @create: 2018-10-10 23:55
 **/
@Aspect
@Component
@Slf4j
public class HttpAspect {

    private final MongoTemplate mongoTemplate;

    // 请求开始时间
    private ThreadLocal<Long> startTime = new ThreadLocal<>();

    // 写入mongodb的日志数据
    private ThreadLocal<WebLog> webLog = new ThreadLocal<>();

    @Autowired
    public HttpAspect(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * request请求方法 切面编程
     */
    @Pointcut("execution(public * com.aron.blog.controller..*.*(..))")
    public void webLogger(){}

    private void saveLogToMongo(WebLog log){
        mongoTemplate.save(log);
    }

    /**
     * weblogger 前置通知
     * @param joinPoint
     */
    @Before("webLogger()")
    public void doBefore(JoinPoint joinPoint){
        long currentTime = System.currentTimeMillis();
        startTime.set(currentTime);
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Signature signature = joinPoint.getSignature();
        // 初始化web log 数据
        WebLog log = new WebLog();
        log.setRequestTime(currentTime);
        log.setUrl(request.getRequestURL().toString());
        log.setClassMethod(signature.getDeclaringTypeName()
                + "." + signature.getName() + "()");
        log.setHttpMethod(request.getMethod());
        log.setIp(IpUtils.getIpAddress(request));
        log.setRequestArgs(JSON.toJSONString(request.getParameterMap()));
        // 保存到线程变量中
        webLog.set(log);
    }

    /**
     * webLogger 后置通知
     * @param ret
     */
    @AfterReturning(returning = "ret", pointcut = "webLogger()")
    public void doAfterReturning(Object ret){
        WebLog log = webLog.get();
        log.setRespData(JSON.toJSONString(ret));
        log.setProcessingTime(String.format("%sms",
                System.currentTimeMillis() - startTime.get()));
        saveLogToMongo(log);
    }

}
