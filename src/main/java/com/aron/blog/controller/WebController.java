package com.aron.blog.controller;

import com.alibaba.fastjson.JSON;
import com.aron.blog.domain.constant.RouteConstant;
import com.aron.blog.domain.constant.WebConstant;
import com.aron.blog.security.utils.RSACoder;
import com.aron.blog.service.RedisService;
import com.aron.blog.utils.Captcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author: Y-Aron
 * @create: 2018-10-27 01:51
 **/
@RestController
@Slf4j
public class WebController {

    private final RedisService redisService;

    @Autowired
    public WebController(RedisService redisService) {
        this.redisService = redisService;
    }

    @GetMapping(RouteConstant.CAPTCHA_URL)
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("rnd:{}", request.getParameter("rnd"));
        Captcha captcha = new Captcha();
        BufferedImage image = captcha.getImage();

        // 将图片验证码放入服务端session(集成redis)
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(WebConstant.CAPTCHA_MAX_TIME);
        session.setAttribute(WebConstant.CAPTCHA_KEY, captcha.getText());

        captcha.output(image, response.getOutputStream()); // 将验证码图片响应给客户端
    }

    @GetMapping("/port/rsa_pk")
    public String getRSAPublicKey() throws Exception {
        String key = WebConstant.RSA_KEY;
        Map<String, String> keyMap ;
        if (redisService.hasKey(key)) {
            keyMap = JSON.parseObject(JSON.toJSONString(redisService.get(key)), Map.class);

        } else {
            keyMap = RSACoder.initKey();
            redisService.set(key, keyMap, 24, TimeUnit.DAYS);
        }
        String publicKey = RSACoder.getPublicKey(keyMap);
        log.info("rsa public key： {}", publicKey);
        return publicKey;
    }
}
