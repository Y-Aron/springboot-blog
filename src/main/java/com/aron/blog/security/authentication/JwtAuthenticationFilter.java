package com.aron.blog.security.authentication;

import com.aron.blog.domain.constant.ResultTypeEnum;
import com.aron.blog.security.utils.JwtUserDetails;
import com.aron.blog.security.utils.JwtUtils;
import com.aron.blog.utils.ResultUtils;
import com.aron.blog.utils.Utils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: Y-Aron
 * @create: 2018-10-12 02:35
 **/
@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private final JwtUtils jwtUtils;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtils jwtUtils){
        super(authenticationManager);
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        log.info("=====JwtAuthenticationFilter=====");
        log.info("URL：{}", request.getRequestURI());
        // 获取请求头上的token信息
        String token = jwtUtils.getToken(request);
        log.info("token：{}", token);

        // 请求头不存在token
        if (token == null) {
            // 资源路径 直接放行
            chain.doFilter(request, response);
            return;
        }

        // 请求头存在token
        try {
            // 全局设置 Authentication
            SecurityContextHolder.getContext().setAuthentication(getAuthentication(token));
        } catch (ExpiredJwtException e) {
            // token 过期
            log.error(e.getMessage());
            Utils.responseResult(response, ResultUtils.fail(ResultTypeEnum.JWT_EXPIRED_FAIL));
            return ;
        } catch (MalformedJwtException e) {
            // token 无效
            log.error(e.getMessage());
            Utils.responseResult(response, ResultUtils.fail(ResultTypeEnum.JWT_INVALID_FAIL));
            return ;
        }
        super.doFilterInternal(request, response, chain);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String token){
        JwtUserDetails userDetails = jwtUtils.validateTokenAndGetUserDetails(token);
        log.info("jwt user details：{}", userDetails);
        if (userDetails != null) {
            return new UsernamePasswordAuthenticationToken(
                    userDetails.getUserId() , null, userDetails.getAuthorities());
        }
        return null;
    }
}
