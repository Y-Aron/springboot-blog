package com.aron.blog.security.authorization;

import com.aron.blog.domain.entity.Role;
import com.aron.blog.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

/**
 * @author: Y-Aron
 * @create: 2018-10-16 02:22
 **/
@Component
public class RestAccessDecision {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final RoleService roleService;

    @Autowired
    public RestAccessDecision(RoleService roleService) {
        this.roleService = roleService;
    }

    public boolean decide(HttpServletRequest request, Authentication authentication) {
        logger.info("===== RestAccessService：decide =====");
        logger.info(request.getRequestURI());
        logger.info(request.getMethod());
        List<Role> roleList = roleService.findRoleByPermission(request.getMethod(),
                request.getRequestURI());
        logger.info("url所需要的角色列表为：{}", roleList);
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (Role role: roleList){
            for (GrantedAuthority s : authorities) {
                if (String.valueOf(role.getId()).equals(s.getAuthority())){
                    return true;
                }
            }
        }
        logger.warn("rest权限验证失败~");
        return false;
    }
}
