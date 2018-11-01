package com.aron.blog.config;

import com.aron.blog.utils.RestSmsUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: Y-Aron
 * @create: 2018-10-11 19:07
 **/
@Configuration
@ConditionalOnProperty(name = "user.smsService.component", havingValue = "ronglianService")
public class RonglianSmsCodeConfig {

    // 初始化主帐号
    @Value("${ronglian.accountSid}")
    private String accountSid;

    // 初始化主账号令牌
    @Value("${ronglian.accountToken}")
    private String accountToken;

    // 初始化服务器地址
    @Value("${ronglian.serverIp}")
    private String serverIp;
    // 初始化服务器端口
    @Value("${ronglian.serverPort}")
    private int serverPort;

    // 初始化应用ID
    @Value("${ronglian.appId}")
    private String appId;

    @Bean
    public RestSmsUtils createPhoneClient() {
        RestSmsUtils phoneClient = new RestSmsUtils();
        phoneClient.setAccount(accountSid, accountToken);
        phoneClient.init(serverIp, serverPort);
        phoneClient.setAppId(appId);
        return phoneClient;
    }
}
