package com.aron.blog.security.utils;

import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * @author: Y-Aron
 * @create: 2018-10-12 23:53
 **/
@ToString
@Setter
public class JwtUserDetails implements UserDetails {

    private Long userId;
    private String username;
    private String password;
    private boolean rememberMe;
    private String captcha;

    private String AES_KEY;

    private Collection<? extends GrantedAuthority> authorities;

    public JwtUserDetails(){}

    public JwtUserDetails(Long userId, String username, String password, List<GrantedAuthority> authorities) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public JwtUserDetails(String username, String password, List<GrantedAuthority> authorities) {
        this(null, username, password, authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Long getUserId() {
        return userId;
    }

    public boolean getRememberMe() {
        return rememberMe;
    }

    public String getCaptcha() {
        if (AES_KEY != null) {
            return AESCoder.decrypt(captcha, AES_KEY);
        }
        return captcha;
    }

    @Override
    public String getPassword() {
        if (AES_KEY != null) {
            return AESCoder.decrypt(password, AES_KEY);
        }
        return this.password;
    }

    @Override
    public String getUsername() {
        if (AES_KEY != null) {
            return AESCoder.decrypt(username, AES_KEY);
        }
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
