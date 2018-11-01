package com.aron.blog.service;

import com.aron.blog.domain.entity.ResultBean;
import com.aron.blog.domain.entity.User;

/**
 * @author Y-Aron
 * @create 2018-10-08 22:17
 */
public interface UserService {

    ResultBean createUser(User user);

    User findByUsername(String username);

    ResultBean checkLogin(String username, String password);
}