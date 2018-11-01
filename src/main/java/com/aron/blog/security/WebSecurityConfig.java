package com.aron.blog.security;

import com.aron.blog.domain.constant.RouteConstant;
import com.aron.blog.security.authentication.CustomAuthentication;
import com.aron.blog.security.authentication.JWTLoginFilter;
import com.aron.blog.security.authentication.JwtAuthenticationFilter;
import com.aron.blog.security.authorization.RestAccessDecision;
import com.aron.blog.security.exception.CustomAccessDeniedHandler;
import com.aron.blog.security.exception.JWTAuthenticationEntryPoint;
import com.aron.blog.security.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 *
 *
 * @author: Y-Aron
 * @create: 2018-10-12 02:28
 **/
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtUtils jwtUtils;

    // 自定义URL权限决策层
    private final RestAccessDecision restAccessDecision;

    // 自定义身份验证供应者
    private final CustomAuthentication authentication;

    @Autowired
    public WebSecurityConfig(JwtUtils jwtUtils, CustomAuthentication authentication,
                             RestAccessDecision restAccessDecision) throws Exception {
        this.jwtUtils = jwtUtils;
        this.authentication = authentication;
        this.restAccessDecision = restAccessDecision;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth){
        auth
                // 实现自定义身份验证
                .authenticationProvider(authentication);
    }

    /**
     * jwt 登陆URL过滤器
     * @return
     */
    @Autowired
    public JWTLoginFilter jwtLoginFilter() {
        try {
            return new JWTLoginFilter(authenticationManager(), jwtUtils);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * jwt 身份验证过滤器
     * @return
     */
    @Autowired
    public JwtAuthenticationFilter jwtAuthenticationFilter(){
        try {
            return new JwtAuthenticationFilter(authenticationManager(), jwtUtils);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 决策层异常处理
     * @return
     */
    @Bean
    public CustomAccessDeniedHandler accessDeniedHandler(){
        return new CustomAccessDeniedHandler();
    }

    /**
     * 端口异常处理
     * @return
     */
    @Bean
    public JWTAuthenticationEntryPoint entryPoint(){
        return new JWTAuthenticationEntryPoint();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 禁用csrf
                .csrf().disable()
                // 禁用session
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 允许WebController端口下的所有URL通过
                .antMatchers("/port/**").permitAll()
                .antMatchers(HttpMethod.GET, RouteConstant.AUTH_LOGIN_URL).denyAll()
                .antMatchers(HttpMethod.GET, RouteConstant.FAVICON_URL).permitAll()
                .antMatchers(HttpMethod.POST, RouteConstant.REGISTER_URL).permitAll()
                // 其他url需要认证
                .anyRequest()
                .access("@restAccessDecision.decide(request, authentication)")
                // 异常处理
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler())
                .authenticationEntryPoint(entryPoint())
                // 添加自定义jwt过滤器
                .and()
                .addFilter(jwtLoginFilter())
                .addFilter(jwtAuthenticationFilter());
    }
}
