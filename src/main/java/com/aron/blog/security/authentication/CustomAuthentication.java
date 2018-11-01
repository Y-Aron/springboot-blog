package com.aron.blog.security.authentication;

import com.aron.blog.domain.constant.ResultTypeEnum;
import com.aron.blog.security.service.CustomUserDetailService;
import com.aron.blog.security.utils.JwtUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * 也可以实现AuthenticationProvider接口 不过这种方式要修改supports()方法。
 * @author: Y-Aron
 * @create: 2018-10-14 04:15
 **/
@Component
public class CustomAuthentication extends DaoAuthenticationProvider {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CustomUserDetailService userDetailService;

    @Autowired
    public CustomAuthentication(CustomUserDetailService userDetailService) {
        this.userDetailService = userDetailService;
        // 设置自定义 UserDetailsService
        this.setUserDetailsService(userDetailService);
        this.setHideUserNotFoundExceptions(false);
    }

    /**
     * 用户名与密码验证 ..
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        logger.info("===== 验证用户登陆信息 =====");
        try{
            JwtUserDetails userDetails = rememberMe(authentication);
            if (userDetails == null) {
                userDetails = checkUser(authentication);
            }
            // 构建返回的用户登录成功的token
            // UsernamePasswordAuthenticationToken(principal, credentials, authorities)
            // principal: 用户持有者
            // credentials: 身份凭证
            // authorities: 角色列表
            return new UsernamePasswordAuthenticationToken(userDetails, null, new ArrayList<>());
        } catch (Exception e){
            logger.error("===== 用户验证失败 =====");
            logger.error("错误信息：{}", e.getMessage());
            throw new BadCredentialsException(e.getMessage());
        }
    }

    /**
     * 没有记住密码标记 则根据表单输入的用户名密码与数据库的用户信息进行校对
     * @param authentication
     * @return
     */
    private JwtUserDetails checkUser(Authentication authentication){
        // 获取JwtUserDetails 上保存的用户登陆信息
        JwtUserDetails principal = (JwtUserDetails) authentication.getPrincipal();

        String userName = principal.getUsername(); // 这个获取表单输入中返回的用户名
        String password = principal.getPassword(); // 这个是表单中输入的密码；

        logger.info("持有者：{}", userName);
        logger.info("凭证：{}", password);

        // 这里构建来判断用户是否存在和密码是否正确
        JwtUserDetails userDetails = (JwtUserDetails) userDetailService.loadUserByUsername(userName);
        // BCryptPasswordEncoder 验证密码是否正确
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        // password 为前端发来的密码数据
        // userDetails.getPassword() 为数据库查询到的密码
        if (!encoder.matches(password, userDetails.getPassword())) {
            logger.error("密码匹配错误~");
            throw new BadCredentialsException(ResultTypeEnum
                    .USER_PASSWORD_FAIL.getMessage());
        }
        // 登陆成功赋值其他登陆信息
        userDetails.setRememberMe(principal.getRememberMe());
        userDetails.setPassword(null);

        return userDetails;
    }

    /**
     * 有记住密码标志则直接根据用户名加载用户信息
     * @param authentication
     * @return
     */
    private JwtUserDetails rememberMe(Authentication authentication) {
        String credentials = (String) authentication.getCredentials();
        if (credentials == null) {
            return null;
        }
        // 这里调用我们的自己写的获取用户的方法；
        return (JwtUserDetails) userDetailService.loadUserByUsername(credentials);
    }


    @Override
    public boolean supports(Class<?> aClass) {
        /*
         * 实现 AuthenticationProvider接口则返回以下形式
         * return CustomAuthentication.class.isAssignableFrom(aClass);
         * 保证 UserDetailsService不被默认的 DaoAuthenticationProvider再次调用
         */
        return true;
    }


}
