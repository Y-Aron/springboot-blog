package com.aron.blog.security.exception;

import com.alibaba.fastjson.JSON;
import com.aron.blog.domain.constant.ResultTypeEnum;
import com.aron.blog.utils.ResultUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException e) throws IOException, ServletException {

        logger.error("===== JWTAuthenticationEntryPoint =====");
        logger.error("页面显示错误消息：{}", e.getMessage());

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write(JSON.toJSONString(
                ResultUtils.fail(ResultTypeEnum.URL_FORBIDDEN)));
    }
}