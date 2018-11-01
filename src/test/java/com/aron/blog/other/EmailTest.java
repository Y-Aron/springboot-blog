package com.aron.blog.other;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * @author: Y-Aron
 * @create: 2018-10-12 00:15
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class EmailTest {
    @Autowired
    private JavaMailSender mailSender; //自动注入的Bean

    @Value("${mail.username}")
    private String Sender; //读取配置文件中的参数

    @Test
    public void sendSimpleMail() {
        SimpleMailMessage message = new SimpleMailMessage();
        System.out.println(Sender);
        message.setFrom(Sender);
        message.setTo("15280553351@163.com"); //自己给自己发送邮件
        message.setSubject("springboot 笔记");
        message.setText("http://ywj.easy.echosite.cn\n" +
                "ab 命令测试 https://www.cnblogs.com/taiyonghai/p/5810150.html\n" +
                "durid配置 https://www.cnblogs.com/wuyun-blog/p/5679073.html\n" +
                "\n" +
                "使用@Aspect注解将一个java类定义为切面类\n" +
                "使用@Pointcut定义一个切入点，可以是一个规则表达式，比如下例中某个package下的所有函数，也可以是一个注解等。\n" +
                "根据需要在切入点不同位置的切入内容\n" +
                "使用@Before在切入点开始处切入内容\n" +
                "使用@After在切入点结尾处切入内容\n" +
                "使用@AfterReturning在切入点return内容之后切入内容（可以用来对处理返回值做一些加工处理）\n" +
                "使用@Around在切入点前后切入内容，并自己控制何时执行切入点自身的内容\n" +
                "使用@AfterThrowing用来处理当切入内容部分抛出异常之后的处理逻辑\n" +
                "\n" +
                "maven 加入本地jar\n" +
                "mvn install:install-file -Dfile=CCP_REST_SMS_SDK_JAVA_v2.6.3r.jar -DgroupId=com.ronglian -DartifactId=ronglian-java-sdk-sms -Dversion=2.6.3 -Dpackaging=jar\n" +
                "yangwjmail@126.com 授权码 yangwenjie1997");
        mailSender.send(message);

    }
}
