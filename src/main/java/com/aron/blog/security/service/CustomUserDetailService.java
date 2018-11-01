package com.aron.blog.security.service;

import com.aron.blog.domain.constant.ResultTypeEnum;
import com.aron.blog.domain.entity.Role;
import com.aron.blog.domain.entity.User;
import com.aron.blog.security.utils.JwtUserDetails;
import com.aron.blog.service.RoleService;
import com.aron.blog.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * @author: Y-Aron
 * @create: 2018-10-12 03:43
 **/
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public CustomUserDetailService(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    /**
     * 加载认证凭借
     * @param username
     * @return
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws BadCredentialsException {
        logger.info("===== 从数据库获取数据 =====");
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("用户：[{}] 不存在", username);
            throw new BadCredentialsException(ResultTypeEnum
                    .USER_NOT_EXIST.getMessage());
        }
        logger.info("查询到的用户信息：{}", user.toString());
        // 获取用户所拥有的角色列表
        List<Role> roles = roleService.findRolesByUserId(user.getId());

        return new JwtUserDetails(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                createAuthorities(roles)
        );
    }

    private List<GrantedAuthority> createAuthorities(List<Role> roles){
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (roles == null || roles.size() == 0) {
            return authorities;
        }
        for (Role role: roles){
            authorities.add(new SimpleGrantedAuthority(String.valueOf(role.getId())));
        }
        return authorities;
    }

}
