package com.aron.blog.other;

import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: Y-Aron
 * @create: 2018-10-10 22:30
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class RedisTest {

//    @Autowired
//    private RedisUtils redisUtils;
//
//    @Test
//    public void testSet(){
//        User user = new User();
//        user.setId(Utils.generateUUID());
//        user.setUsername("admin");
//        user.setPassword("123456");
//        boolean result = redisUtils.set("user", user);
//        System.out.println(result);
//    }
//
//    @Test
//    public void testSetTime(){
//        User user = new User();
//        user.setId(Utils.generateUUID());
//        user.setUsername("admin");
//        user.setPassword("123456");
//        boolean result = redisUtils.set("user1", user, 100);
//        System.out.println(result);
//    }
//
//    @Test
//    public void testGet(){
//        System.out.println(redisUtils.get("user"));
//    }
//
//    @Test
//    public void testClearCache(){
//        boolean result = redisUtils.clearByPattern("CACHE::*");
//        System.out.println(result);
//    }
//
//    @Test
//    public void testIncr(){
//        long number = redisUtils.incr("number", 3);
//        System.out.println(number);
//    }
}
