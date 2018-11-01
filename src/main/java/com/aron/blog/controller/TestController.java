package com.aron.blog.controller;

import com.aron.blog.domain.entity.User;
import com.aron.blog.service.SmsCodeService;
import com.aron.blog.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Y-Aron
 * @create 2018-10-08 21:56
 */
@RestController
@Slf4j
public class TestController {

    private final SmsCodeService smsCodeService;

    @Autowired
    public TestController(SmsCodeService smsCodeService) {
        this.smsCodeService = smsCodeService;
    }

    @PostMapping("/ex")
    public String testEx(@RequestParam("num") int num){
//        log.info(message);
        log.info("结果：{}", 1/num);
        return "num: "+ num;
    }

    @GetMapping(value = "/test/redis/set")
    public String index(HttpServletRequest request) {
        System.out.println(request.toString());
        System.out.println(request.getRequestURL());
        System.out.println(request.getMethod());
        System.out.println(request.getRequestURI());
        User user = new User();
        user.setId(Utils.generateUUID());
        user.setUsername("admin");
        user.setPassword("123456");
        return "test ok";
    }

    @PostMapping("/test/mobile")
    public Object test(@RequestParam("mobile") String mobile){
        System.out.println(mobile);
        return smsCodeService.sendTemplateSMS(mobile);
    }
}