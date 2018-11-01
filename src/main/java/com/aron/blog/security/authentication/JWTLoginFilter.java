package com.aron.blog.security.authentication;

import com.aron.blog.domain.constant.ResultTypeEnum;
import com.aron.blog.domain.constant.RouteConstant;
import com.aron.blog.domain.constant.WebConstant;
import com.aron.blog.security.utils.JwtUserDetails;
import com.aron.blog.security.utils.JwtUtils;
import com.aron.blog.utils.ResultUtils;
import com.aron.blog.utils.Utils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 登陆URL过滤器
 * @author: Y-Aron
 * @create: 2018-10-13 00:09
 **/

public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private AuthenticationManager authenticationManager;

    private static final String USER_NAME_KEY = "username";
    private static final String USER_PASSWORD_KEY = "password";
    private static final String REMEMBER_ME = "rememberMe";
    private static final String CAPTCHA = "captcha";
    private static final int REMEMBER_ME_MAX_TIME = 60 * 60 * 24;

    private JwtUtils jwtUtils;

    public JWTLoginFilter(AuthenticationManager authenticationManager,
                          JwtUtils jwtUtils) {
        super.setFilterProcessesUrl(RouteConstant.AUTH_LOGIN_URL);
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    /**
     * 接收并解析用户凭证，attemptAuthentication ：接收并解析用户凭证
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        logger.info("===== 接收用户登陆信息 =====");
        if (HttpMethod.GET.matches(request.getMethod())){
            logger.error("{} url不支持GET请求~", RouteConstant.AUTH_LOGIN_URL);
            throw new UsernameNotFoundException(ResultTypeEnum
                    .USER_URL_METHOD_NOT_ALLOWED.getMessage());
        }
        // 记住密码直接登录
        final Object hasUsername = request.getSession().getAttribute(REMEMBER_ME);
        if (hasUsername != null) {
            logger.info("用户名[{}]记住密码直接登陆", hasUsername);
            return generateAuthenticate(null, (String)hasUsername);
        }

        JwtUserDetails userDetails = verifyUserDetails(request);
        return generateAuthenticate(userDetails, null);
    }
    /**
     * 记住密码
     * @param request
     * @param userDetails
     */
    private void rememberMe(HttpServletRequest request, JwtUserDetails userDetails){
        // 记住信息
        if (userDetails.getRememberMe()) {
            HttpSession session = request.getSession();
            session.setAttribute(REMEMBER_ME, userDetails.getUsername());
            session.setMaxInactiveInterval(REMEMBER_ME_MAX_TIME);
        }
    }

    /**
     * 登陆成功后在响应头上设置token
     * @param request
     * @param response
     * @param chain
     * @param authResult
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult){
        logger.info("===== 用户登陆成功 =====");
        JwtUserDetails userDetails = (JwtUserDetails) authResult.getPrincipal();
        logger.info("jwt user details: {}", userDetails);

        String token = jwtUtils.generateTokenByUserDetails(userDetails);
        logger.info("用户[{}]登陆成功~ token：{}", userDetails.getUsername(), token);
        rememberMe(request, userDetails);
        jwtUtils.setToken(response, token);
        Utils.responseResult(response, ResultUtils.success(ResultTypeEnum.USER_LOGIN_SUCCESS));
    }

    /**
     * 处理失败响应
     * @param request
     * @param response
     * @param e
     * @throws IOException===== 登陆错误信息 =====
     * @throws ServletException
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException e) throws IOException {
        logger.error("===== 登陆错误信息 =====");
        logger.error("错误信息：{}", e.getMessage());
        Utils.responseResult(response, ResultUtils.fail(e.getMessage()));
    }

    /**
     * 验证登陆信息是否合格
     * 验证图形验证码是否正确
     * 验证用户名、密码不能为空
     * @param request
     * @return
     */
    private JwtUserDetails verifyUserDetails(HttpServletRequest request) {
        JwtUserDetails userDetails = generateUser(request);
        String AES_KEY = request.getHeader(WebConstant.AES_SECRET_KEY);

        if (AES_KEY != null) {
            AES_KEY = jwtUtils.decryptAES(AES_KEY);
            logger.info("AES_KEY：{}", AES_KEY);
            if (AES_KEY == null) {
                throw new UsernameNotFoundException(ResultTypeEnum.FAIL.getMessage());
            }
            userDetails.setAES_KEY(AES_KEY);
        }
        logger.info("接收到的用户信息：{}", userDetails.toString());

        verifyCaptcha(request, userDetails);
        // 用户不能为空
        if (StringUtils.isBlank(userDetails.getUsername())){
            throw new UsernameNotFoundException(
                    ResultTypeEnum.USER_NOT_NULL.getMessage());
        }
        // 密码不能为空
        if (StringUtils.isBlank(userDetails.getPassword())) {
            throw new UsernameNotFoundException(
                    ResultTypeEnum.USER_PASSWORD_NOT_NULL.getMessage());
        }
        return userDetails;
    }

    /**
     * 从session中获取图形验证码与验证码进行校对
     * @param request
     * @param userDetails
     */
    private void verifyCaptcha(HttpServletRequest request, JwtUserDetails userDetails){
        // 获取session(整合redis实现共享)
        String _captcha = (String) request.getSession()
                .getAttribute(WebConstant.CAPTCHA_KEY);

        // 服务器重启的话服务端的captcha会消失. 可升级为redis存储
        logger.info("服务端 captcha：{}", _captcha);
        // 验证码不能为空
        if (_captcha == null) {
            throw new UsernameNotFoundException(
                    ResultTypeEnum.CAPTCHA_NOT_NULL.getMessage());
        }
        // 判断验证码是否正确 转成小写判断
        if (!_captcha.toLowerCase().equals(userDetails.getCaptcha())) {
            throw new UsernameNotFoundException(
                    ResultTypeEnum.CAPTCHA_FAIL.getMessage());
        } else {
            request.getSession().removeAttribute(WebConstant.CAPTCHA_KEY);
        }
    }

    /**
     * 从HttpServletRequest 中读取登陆信息 支持json数据与表单数据
     * @param request
     * @return
     */
    private JwtUserDetails generateUser(HttpServletRequest request) {

        if (request.getHeader("Content-Type").startsWith("application/json")) {
            // 获取json数据
            return (JwtUserDetails) Utils.getJSONObject(request, JwtUserDetails.class);
        } else {
            // 获取表单数据
            return new JwtUserDetails(){{
                setUsername(request.getParameter(USER_NAME_KEY));
                setPassword(request.getParameter(USER_PASSWORD_KEY));
                setRememberMe(BooleanUtils.toBoolean(request.getParameter(REMEMBER_ME)));
                setCaptcha(request.getParameter(CAPTCHA));
            }};
        }
    }


    private Authentication generateAuthenticate(JwtUserDetails userDetails, String username){
        try{
            // 生成用户凭证
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    userDetails, username,
                    new ArrayList<>()));

        } catch (Exception e){
            logger.error("用户登陆生成凭证失败：{}", e.getMessage());
            throw new AuthenticationServiceException(e.getMessage());
        }
    }
}
