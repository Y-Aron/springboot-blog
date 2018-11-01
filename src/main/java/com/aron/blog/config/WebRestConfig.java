package com.aron.blog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author: Y-Aron
 * @create: 2018-10-12 02:52
 **/
@Configuration
public class WebRestConfig  implements WebMvcConfigurer {

    /**
     * 配置启动时执行方法
     * @return
     */
//    @Bean
//    CustomCmdLineRunner customCmdLineRunner(){
//        return new CustomCmdLineRunner();
//    }

//    @Bean
//    @ConditionalOnMissingBean(value = ErrorController.class, search = SearchStrategy.CURRENT)
//    public BasicErrorController basicErrorController(ErrorAttributes errorAttributes) {
//        return null;
//    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    }

    /**
     * 开启全局请求跨域
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedHeaders("*")
                .allowedOrigins("*")
                .allowedMethods("OPTIONS", "GET", "POST", "PUT", "DELETE");
    }

}