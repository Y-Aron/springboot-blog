package com.aron.blog.security.exception;

import com.alibaba.fastjson.JSON;
import com.aron.blog.domain.constant.ResultTypeEnum;
import com.aron.blog.utils.ResultUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException {
        logger.error("===== CustomAccessDeniedHandler =====");
        logger.error("错误信息：{}", e.getMessage());
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        response.getWriter().write(JSON
                .toJSONString(ResultUtils.fail(ResultTypeEnum.URL_FORBIDDEN)));
    }
}