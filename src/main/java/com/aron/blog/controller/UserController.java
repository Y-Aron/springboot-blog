package com.aron.blog.controller;

import com.aron.blog.domain.entity.User;
import com.aron.blog.service.UserService;
import com.aron.blog.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Y-Aron
 * @create 2018-10-08 22:24
 */
@RestController
@Slf4j
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/register")
    public Object register(@RequestParam("username") String username,
                         @RequestParam("password") String password){
        User user = new User();
        user.setId(Utils.generateUUID());
        user.setUsername(username);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(password));
        return userService.createUser(user);
    }

    @GetMapping("/userList")
    public List<User> fetchUser(@RequestParam("id") long id){
        return null;
//        long id = 315480732598272L;
//        return userService;
    }

}