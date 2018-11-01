package com.aron.blog.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author Y-Aron
 * @create 2018-10-09 21:55
 */
@Configuration
public class DruidConfig {

    private static final String USER_NAME = "root";
    private static final String PASSWORD = "root";
    private static final String ALLOW = "*";

    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource getDruidDataSource(){
        return new DruidDataSource();
    }

//    @Bean
//    public ServletRegistrationBean druidStatViewServlet(){
//        ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
//        Map<String, Object> initParams = new HashMap<>();
//        // 登陆用户名
//        initParams.put("loginUsername", USER_NAME);
//        // 登陆密码
//        initParams.put("loginPassword", PASSWORD);
//        // 白名单
//        initParams.put("allow", ALLOW);
//        // 黑名单
////        initParams.put("deny", "192.168.15.21");
//        bean.setInitParameters(initParams);
//        return bean;
//    }

//    @Bean
//    public FilterRegistrationBean webStatFilter() {
//        FilterRegistrationBean bean = new FilterRegistrationBean();
//        bean.setFilter(new WebStatFilter());
//        bean.addUrlPatterns("/*");
//        bean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
//        return bean;
//    }
}