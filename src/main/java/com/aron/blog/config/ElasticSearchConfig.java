package com.aron.blog.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author: Y-Aron
 * @create: 2018-10-23 22:58
 **/
@Configuration
@Slf4j
public class ElasticSearchConfig implements FactoryBean<RestClient>, InitializingBean, DisposableBean {

    /**
     * es集群 ip地址
     */
    @Value("${elasticsearch.ip}")
    private String host;

    /**
     * 端口号
     */
    @Value("${elasticsearch.port}")
    private int port;


    private RestClient client;

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            RestClientBuilder builder = RestClient.builder(new HttpHost("47.107.42.56", 9200));
            builder.setMaxRetryTimeoutMillis(10000);
            client = builder.build();
        } catch (Exception e) {
            log.error("elasticSearch TransportClient create error~: {}", e.getMessage());
        }
    }

    @Override
    public void destroy() {
        try {
            log.info("Closing elasticSearch client");
            if (client != null) {
                client.close();
            }

        } catch (Exception e) {
            log.error("Error closing ElasticSearch client: {}", e.getMessage());
        }
    }

    @Override
    public RestClient getObject() throws Exception {
        return client;
    }

    @Override
    public Class<?> getObjectType() {
        return RestClient.class;
    }
}
