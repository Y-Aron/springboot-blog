package com.aron.blog.other;

import com.aron.blog.domain.entity.User;
import com.aron.blog.utils.Utils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: Y-Aron
 * @create: 2018-10-11 00:55
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class MongoTest {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void testSave(){
        User user = new User();
        user.setId(Utils.generateUUID());
        user.setUsername("admin");
        user.setPassword("123456");
        mongoTemplate.save(user,"users");
        mongoTemplate.save(user);
    }
}
