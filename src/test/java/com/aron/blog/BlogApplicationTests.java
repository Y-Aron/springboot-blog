package com.aron.blog;

import com.aron.blog.security.utils.AESCoder;
import com.aron.blog.service.impl.RonglianServiceImpl;
import com.aron.blog.utils.RestSmsUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;


//@RunWith(SpringRunner.class)
//@SpringBootTest
public class BlogApplicationTests {

    @Value("${user.smsCode.smsCodelen}")
    private String message;

    @Autowired
    private RonglianServiceImpl ronglianService;

    @Test
    public void testSms(){
        RestSmsUtils client = new RestSmsUtils();
        client.init("app.cloopen.com", 8883);
        client.setAccount("8aaf0708624670f2016265f7a2dc0b6e", "ba53a365477640c7a4c05c8ac62a13bc");
        client.setAppId("8aaf0708624670f2016265f7a3330b74");
        Map<String, Object> map = client.sendTemplateSMS("15280553351", "1", new String[]{"12345", "3"});
        System.out.println(map);
    }

    @Test
    public void test() throws Exception {
        String key = "yAFjdZKfWjBRG3fC";
        final String decrypt = AESCoder.decrypt("x34+wL2mcp/X4GK8Qicx4w==", key);
        System.out.println(decrypt);

    }
}
