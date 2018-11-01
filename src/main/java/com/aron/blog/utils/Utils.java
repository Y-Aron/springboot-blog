package com.aron.blog.utils;

import com.alibaba.fastjson.JSON;
import com.aron.blog.domain.entity.ResultBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Random;

/**
 * @author Y-Aron
 * @create 2018-10-08 22:28
 */
@Slf4j
public class Utils {

    /**
     * snowflake算法对象
     */
    private static SnowFlake snowflake;

    /**
     * 生成唯一ID
     * @return long id
     */
    public static long generateUUID() {
        if (snowflake == null) {
            snowflake = new SnowFlake(2, 3);
        }
        return snowflake.nextId();
    }

    /**
     * 生成随机验证码
     * @param length
     * @return
     */
    public static String generateValidateCode(int length){
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        String source = "2345678abcdefhijkmnpqrstuvwxyzABCDEFGHJKLMNPQRTUVWXY";
        for (int i = 0; i< length; i++){
            int tmp = random.nextInt(source.length());
            sb.append(source.charAt(tmp));
        }
        return sb.toString();
    }

    public static Object getJSONObject(HttpServletRequest request, Class<?> T){
        try {
            return new ObjectMapper()
                    .readValue(request.getInputStream(), T);
        } catch (IOException ex) {
            try {
                return T.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static void responseResult(HttpServletResponse response, ResultBean resp){
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        try {
            response.getWriter().write(JSON.toJSONString(resp));
        } catch (IOException e) {
            log.error("响应失败：{}", e.getMessage());
        }
    }


    public static void main(String[] args) throws IOException {
        String BASE_DIR = System.getProperty("user.dir");
        String path = BASE_DIR + "/src/main/resources/application.yml".replace("/", File.separator);
        InputStream inputStream = new FileInputStream(new File(path));
        Yaml yaml = new Yaml();
//        Map<String, Object> data = (Map<String, Object>) yaml.loadAll(inputStream);
        Map<String, Object> data = yaml.load(inputStream);
        for (String key: data.keySet()) {
            System.out.println(key);
            System.out.println(data.getClass());
            System.out.println(data.get(key));
        }
        System.out.println(data);
//        Map<String, Object> newData = new HashMap<>();
//        newData.put("url", "jdbc:mysql://localhost:3306/test");
//        newData.put("username", "root1");
//        newData.put("password", "admin1");
//        for (String key: newData.keySet()){
//            if (data.containsKey(key)){
//                data.replace(key, newData.get(key));
//            }
//        }
//        Writer writer = new FileWriter(new File(path));
//        yaml.dump(data, writer);
    }
}