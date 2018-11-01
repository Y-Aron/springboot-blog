package com.aron.blog.utils;

import com.alibaba.fastjson.JSON;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.util.DigestUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Y-Aron
 * @create: 2018-10-11 22:01
 **/

public class RestSmsUtils {

    private String accountSid;
    private String accountToken;
    private String serverIp;
    private int serverPort;
    private String appId;

    private String currentTime;

    public RestSmsUtils(){
        // 初始化时间格式
        this.currentTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }

    /**
     * 发送短信验证码
     * @param mobile 手机号
     * @param templateCode 短信模版代码
     * @param data 短信数据
     * @return
     */
    public Map<String, Object> sendTemplateSMS
            (String mobile, String templateCode, String[] data){
        Map<String, Object> body = this.initBody(mobile, templateCode, data);
        MultiValueMap<String, String> headers = this.initHeaders();
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        String url = this.initURL();
        ResponseEntity<String> resp = restTemplate.postForEntity(url, httpEntity, String.class);

        return JSON.parseObject(resp.getBody());
    }

    /**
     * 初始化请求URL
     * @return
     */
    private String initURL(){
        String temp = accountSid + accountToken + currentTime;
        String signature = DigestUtils.md5DigestAsHex(temp.getBytes()).toUpperCase();
        String afterUrl = String.format("2013-12-26/Accounts/%s/SMS/TemplateSMS?sig=%s",
                accountSid, signature);
        return String.format("https://%s:%s/%s", serverIp, serverPort, afterUrl);
    }


    private Map<String, Object> initBody(String mobile, String templateCode, String[] data){
        Map<String, Object> body = new HashMap<>();
        body.put("to", mobile);
        body.put("appId", appId);
        body.put("templateId", templateCode);
        body.put("datas", data);
        return body;
    }

    /**
     * 初始化请求头
     * @return
     */
    private MultiValueMap<String, String> initHeaders(){
        String temp = accountSid + ":" + currentTime;
        String authorization = Base64Utils.encodeToString(temp.getBytes());
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        headers.add("Content-Type", "application/json;charset=utf-8");
        headers.add("content-Length", String.valueOf(256));
        headers.add("authorization", authorization);
        return headers;
    }

    /**
     * 初始化服务器和端口
     * @param serverIp
     * @param serverPort
     */
    public void init(String serverIp, int serverPort){
        this.serverIp = serverIp;
        this.serverPort = serverPort;
    }

    /**
     * 初始化主账号和令牌
     * @param accountSid
     * @param accountToken
     */
    public void setAccount(String accountSid, String accountToken){
        this.accountSid = accountSid;
        this.accountToken = accountToken;
    }

    /**
     * 初始化应用ID
     * @param appId
     */
    public void setAppId(String appId){
        this.appId = appId;
    }

}
