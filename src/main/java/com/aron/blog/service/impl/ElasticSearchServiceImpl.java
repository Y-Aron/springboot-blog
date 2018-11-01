package com.aron.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.aron.blog.service.ElasticSearchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseException;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

/**
 * @author: Y-Aron
 * @create: 2018-10-24 01:55
 **/
@Service
@Slf4j
public class ElasticSearchServiceImpl implements ElasticSearchService {

    private final RestClient client;

    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String PUT = "PUT";
    private static final String DELETE = "DELETE";
    private static final String ID = "_id";
    private static final String ACKNOWLEDGED = "acknowledged";
    private static final String SEARCH = "_search";
    private static final String UPDATE = "_update";
    private static final String RESULT = "result";
    private static final String DELETED = "deleted";
    private static final int OK_CODE = 200;

    private JSONObject DEFAULT_INDEX_SETTING = new JSONObject(){{
        put("settings", new JSONObject(){{
            put("number_of_shards", 3);
            put("number_of_replicas", 1);
        }});
    }};


    private enum ResultType {
        ACKNOWLEDGED, IS_FOUND, IS_CREATED, IS_UPDATED
    }

    @Autowired
    public ElasticSearchServiceImpl(RestClient client) {
        this.client = client;
    }

    @Override
    public JSONObject matchQuery(String index, String type, String json) {
        try {
            // 校验JSON格式是否正确 ~
            JSON.parseObject(json);
            // 默认查询所有列表
            return query(index, type, json);
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        } catch (JSONException e) {
            log.error("JSON格式不正确~");
            return null;
        }
    }

    @Override
    public JSONObject matchQuery(String index, String type, Map<String, ?> params) {
        return null;
    }

    @Override
    public String setSource(String index, String type, Object source) {
        return this.setSource(index, type, null, source);
    }

    @Override
    public String setSource(String index, String type, String id, Object source) {
        String url = initUrl(index, type, id);
        if (url == null) {
            return null;
        }

        if (source == null) {
            log.error("数据：{} 不为 null", source);
            return null;
        }

        try {
            String _id = baseSetSource(url, source, id);
            if (_id != null) {
                log.error("索引[{}]=>文档[{}]=>数据[{}]插入成功：ID[{}]", index, type, source, _id);
            } else {
                log.error("索引[{}]=>文档[{}]=>数据[{}]插入失败~", index, type, source);
            }
            return _id;
        } catch (IOException e) {
            log.error("索引[{}]=>文档[{}]=>数据[{}]插入失败：{}", index, type, source, e.getMessage());
            return null;
        }
    }

    @Override
    public boolean buildIndex(String index, Map<String, ?> map) {
        if (isIndexExist(index)) {
            log.error("索引[{}]已存在~", index);
            return false;
        }

        if (MapUtils.isNotEmpty(map)) {
            DEFAULT_INDEX_SETTING.putAll(map);
        }

        HttpEntity entity = new NStringEntity(JSON.toJSONString(DEFAULT_INDEX_SETTING), ContentType.APPLICATION_JSON);
        // put 请求 ES索引不支持大写
        Request request = new Request(PUT, "/" + index.toLowerCase());
        request.setEntity(entity);
        try {
            log.info("索引[{}]创建成功", index);
            return (boolean) handlerResult(request, ResultType.ACKNOWLEDGED);
        } catch (IOException e) {
            log.error("索引[{}]创建失败：{}", index, e.getMessage());
            return false;
        }
    }

    @Override
    public boolean buildIndex(String index) {
        return this.buildIndex(index, null);
    }

    @Override
    public boolean isIndexExist(String index) {
        if (StringUtils.isEmpty(index)) {
            log.error("索引[{}]不能为空~", index);
            return false;
        }

        Request request = new Request(GET, "/" + index.toLowerCase());

        try {
            Response response = client.performRequest(request);
            return response.getStatusLine().getStatusCode() == OK_CODE;

        } catch (ResponseException e) {
//            log.error("索引[{}]不存在~", index);
            return false;

        } catch (IOException e) {
            log.error("连接失败：{}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteIndex(String index) {
        if (!isIndexExist(index)) {
            log.error("索引[{}]不存在~", index);
            return false;
        }
        Request request = new Request(DELETE, "/" + index.toLowerCase());
        try {
            log.error("索引[{}]删除成功", index);
            return (boolean) handlerResult(request, ResultType.ACKNOWLEDGED);
        } catch (IOException e) {
            log.error("索引[{}]删除失败：", index, e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delSource(String index, String type, String id) {
        String url = initUrl(index, type, id);
        if (url == null) {
            return false;
        }

        if (StringUtils.isEmpty(id)) {
            log.error("id[{}]不能为空~", id);
            return false;
        }

        Request request = new Request(DELETE, url);

        try {
            log.error("索引[{}]=>文档[{}]=>ID[{}]删除成功", index, type, id);
            return (boolean) handlerResult(request, ResultType.IS_FOUND);
        } catch (IOException e) {
            log.error("索引[{}]=>文档[{}]=>ID[{}]删除失败：{}", index, type, id, e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateSource(String index, String type, String id, Object source) {
        String url = initUrl(index, type, id);
        if (url == null) {
            return false;
        }
        JSONObject jsonObject = new JSONObject();

        if (source.getClass().equals(String.class)) {
            jsonObject.put("doc", JSON.parseObject((String) source));
        } else {
            jsonObject.put("doc", source);
        }

        HttpEntity entity = new NStringEntity(JSON.toJSONString(jsonObject), ContentType.APPLICATION_JSON);
        Request request = new Request(POST, url + "/" + UPDATE);
        request.setEntity(entity);
        try {
            String value = (String) handlerResult(request, ResultType.IS_UPDATED);
            if ("noop".equals(value)) {
                log.info("索引[{}]=>文档[{}]=>数据[{}]无更新~", index, type, source);
            } else {
                log.info("索引[{}]=>文档[{}]=>数据[{}]更新成功~", index, type, source);
            }
            return true;

        } catch (IOException e) {
            log.error("索引[{}]=>文档[{}]=>数据[{}]插入失败：{}", index, type, source, e.getMessage());
            return false;
        }
    }

    private Object handlerResult(Request request, ResultType type) throws IOException {

        Response response = client.performRequest(request);
        JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity()));

        if (type == ResultType.IS_CREATED) {
            if (result.containsKey(ID)) {
                return result.getString(ID);
            }
            return null;
        }

        if (type == ResultType.ACKNOWLEDGED) {
            // 判断是否有ACKNOWLEDGED属性
            return result.containsKey(ACKNOWLEDGED);
        }

        if (type == ResultType.IS_FOUND) {
            if (result.containsKey(RESULT)) {
                return result.getString(RESULT).equals(DELETED);
            } else {
                return false;
            }
        }

        if (type == ResultType.IS_UPDATED) {
            if (result.containsKey(RESULT)) {
                return result.getString(RESULT);
            }
        }

        return null;
    }

    private JSONObject query(String index, String type, String json) throws IOException {
        String url = initUrl(index, type, null);
        if (url == null) {
            return null;
        }

        Request request = new Request(GET, url + "/" + SEARCH);

        if (StringUtils.isNotEmpty(json)) {
            request.setEntity(new NStringEntity(json, ContentType.APPLICATION_JSON));
        }
        Response response = client.performRequest(request);
        return JSON.parseObject(EntityUtils.toString(response.getEntity()));
    }

    private String baseSetSource(String url, Object object, String id) throws IOException {

        HttpEntity httpEntity = new NStringEntity(JSON.toJSONString(object),
                ContentType.APPLICATION_JSON);

        Request request;
        if (StringUtils.isNotEmpty(id)) {
            // PUT
            request = new Request(PUT, url);

        } else {
            // POST
            request = new Request(POST, url);
        }
        request.setEntity(httpEntity);

        return (String) handlerResult(request, ResultType.IS_CREATED);
    }

    private String initUrl(String index, String type, String id) {
        StringBuilder sb = new StringBuilder();
        // index != null or index != ""
        if (StringUtils.isBlank(index)) {
            log.error("索引[{}]不能为空~", index);
            return null;
        } else {
            sb.append("/").append(index);
        }
        // type != null or type != ""
        if (StringUtils.isBlank(type)) {
            log.error("文档类型[{}]不能为空~", type);
            return null;
        } else {
            sb.append("/").append(type);
        }

        if (StringUtils.isNotEmpty(id)) {
            sb.append("/").append(id);
        }
        return sb.toString();
    }
}
