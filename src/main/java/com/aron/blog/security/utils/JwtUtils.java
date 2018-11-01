package com.aron.blog.security.utils;

import com.alibaba.fastjson.JSON;
import com.aron.blog.domain.constant.WebConstant;
import com.aron.blog.service.RedisService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author: Y-Aron
 * @create: 2018-10-12 02:26
 **/
@Component
public class JwtUtils {

    // token 有效时间
    @Value("${jwt.timeout}")
    private Long timeOut;

    // token 秘钥
    @Value("${jwt.secret}")
    private String secret;

    // token 持有者
    @Value("${jwt.token-holder}")
    private String tokenHolder;

    // token 请求头
    @Value("${jwt.token-header}")
    private String tokenHeader;

    private static final String SEPARATOR = "&";
    private static final String PAYLOAD_USER_NAME = "username";
    private static final String PAYLOAD_USER_ID = "user_id";
    private static final String PAYLOAD_USER_ROLES = "user_roles";

    private final RedisService redisService;

    @Autowired
    public JwtUtils(RedisService redisService) {
        this.redisService = redisService;
    }

    // 根据request获取token
    public String getToken(HttpServletRequest request) {
        return request.getHeader(this.tokenHeader);
    }

    // 设置token
    public void setToken(HttpServletResponse response, String token) {
        response.addHeader(this.tokenHeader, this.tokenHolder.concat(token));
    }

    public String decryptAES(String encryptAES){
        Map<String, String> keyMap = JSON.parseObject(
                JSON.toJSONString(redisService.get(WebConstant.RSA_KEY)), Map.class);

        final String privateKey = RSACoder.getPrivateKey(keyMap);
        if (privateKey != null) {
            return RSACoder.decrypt(encryptAES, privateKey);
        }
        return null;
    }

    /**
     * @description 生成token信息
     * @param claims payload 有效载荷：自定义数据
     * @return
     */
    public String generateToken(Map<String, Object> claims) {
        return this.generateToken(claims, null);
    }

    public String generateToken(Map<String, Object> claims, Long timeOut) {
        if (timeOut == null) {
            timeOut = this.timeOut;
        }
        // 生成过期时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND, timeOut.intValue());
        Date expiration = calendar.getTime();
        // 生成jwt
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }


    /**
     * @description 验证token信息
     * @param token
     * @return
     */
    public JwtUserDetails validateTokenAndGetUserDetails(String token){
        Map<String, Object> claims = validateTokenAndGetClaims(token);

        if (MapUtils.isEmpty(claims)) {
            return null;
        }
        String username = (String) claims.get(PAYLOAD_USER_NAME);
        String user_roles = (String) claims.get(PAYLOAD_USER_ROLES);
        if (StringUtils.isNotBlank(user_roles)){
            return new JwtUserDetails(username, null, decodeAuthorities(user_roles));
        }
        return null;
    }

    /**
     * 由jwtUserDetails 为载荷 创建token
     * @param userDetails
     * @return
     */
    public String generateTokenByUserDetails(JwtUserDetails userDetails){
        Map<String, Object> claims = null;
        if (userDetails != null) {
            claims = initPayLoad(userDetails);
        }
        return generateToken(claims);
    }

    private Map<String, Object> validateTokenAndGetClaims(String token) {
        if (token == null) {
            return null;
        }
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token.replace(tokenHolder, ""))
                .getBody();
    }

    private List<GrantedAuthority> decodeAuthorities(String user_roles) {
        if (StringUtils.isBlank(user_roles)) {
            return new ArrayList<>();
        }
        String[] roleList = user_roles.split(SEPARATOR);

        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role: roleList) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }

    private Map<String, Object> initPayLoad(JwtUserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        claims.put(PAYLOAD_USER_NAME, userDetails.getUsername());
        claims.put(PAYLOAD_USER_ID, userDetails.getUserId());
        claims.put(PAYLOAD_USER_ROLES, encodeAuthorities(userDetails.getAuthorities()));
        return claims;
    }

    private String encodeAuthorities(Collection<? extends GrantedAuthority> authorities){
        StringBuilder sb = new StringBuilder();
        for (GrantedAuthority s : authorities) {
            sb.append(s.getAuthority()).append(SEPARATOR);
        }
        return sb.toString();
    }
}
