package com.aron.blog.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Y-Aron
 * @create 2018-10-08 21:56
 */
@RestController
public class IndexController {

    @GetMapping("/api/test/index")
    public String test(){

        return "ok";
    }
}