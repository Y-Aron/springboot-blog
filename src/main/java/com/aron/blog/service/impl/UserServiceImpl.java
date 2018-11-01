package com.aron.blog.service.impl;

import com.aron.blog.domain.constant.ResultTypeEnum;
import com.aron.blog.domain.mapper.UserMapper;
import com.aron.blog.domain.entity.ResultBean;
import com.aron.blog.domain.entity.User;
import com.aron.blog.security.utils.JwtUtils;
import com.aron.blog.service.UserService;
import com.aron.blog.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Y-Aron
 * @create 2018-10-08 22:17
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final JwtUtils jwtUtils;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, JwtUtils jwtUtils) {
        this.userMapper = userMapper;
        this.jwtUtils = jwtUtils;
    }

    @CachePut(value = "user", key = "#user.id")
    @Override
    public ResultBean createUser(User user) {
        User resultUser = userMapper.findUser(user.getUsername());

        if (resultUser == null) {
            if (userMapper.create(user) > 0){
                return ResultUtils.success(ResultTypeEnum.USER_REGISTER_SUCCESS);
            } else {
                return ResultUtils.fail(ResultTypeEnum.USER_REGISTER_FAIL);
            }
        }
        return ResultUtils.fail(ResultTypeEnum.USER_ALREADY_EXISTS);
    }

//    @Cacheable(cacheNames = "user", key = "#username")
    @Override
    public User findByUsername(String username) {
        return userMapper.findUser(username);
    }

    @Override
    public ResultBean checkLogin(String username, String password) {
        User user = userMapper.findUser(username);

        if (user == null){
            return ResultUtils.fail(ResultTypeEnum.USER_NOT_EXIST);
        }
        String encodePassword = new BCryptPasswordEncoder().encode(user.getPassword());
        if (!encodePassword.equals(password)){
            return ResultUtils.fail(ResultTypeEnum.USER_PASSWORD_FAIL);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("userId", user.getId());
        map.put("userName", user.getUsername());

        String token = jwtUtils.generateToken(map);
        map.clear();
        map.put("token", token);
        return ResultUtils.success(ResultTypeEnum.SEND_SMS_ERROR, map);
    }
}