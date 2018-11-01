package com.aron.blog.service;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @author: Y-Aron
 * @create: 2018-10-24 01:53
 **/
public interface ElasticSearchService {

    /**
     * elasticsearch dsl语法查询
     * @param index 索引名称
     * @param type 文档类型
     * @param json json数据(dsl语法)
     * @return
     */
    JSONObject matchQuery(String index, String type, String json);

    /**
     * 根据URL参数查询
     * @param index 索引名称
     * @param type 文档类型
     * @param params URL参数
     * @return
     */
    JSONObject matchQuery(String index, String type, Map<String, ?> params);

    /**
     * 增加文档数据 dsl语法
     * @param index 索引名称
     * @param type 文档类型
     * @param source 数据
     * @return
     */
    String setSource(String index, String type, Object source);

    /**
     * PUT增加文档 -> 先删除后添加
     * @param index 索引名称
     * @param type 文档类型
     * @param id  _id
     * @param source 数据
     * @return
     */
    String setSource(String index, String type, String id, Object source);

    /**
     * 创建索引
     * @param index 索引名称
     * @param settings 索引配置
     * @return
     */
    boolean buildIndex(String index, Map<String, ?> settings);

    boolean buildIndex(String index);

    /**
     * 判断索引是否存在
     * @param index 索引名称
     * @return
     */
    boolean isIndexExist(String index);

    /**
     * 删除索引
     * @param index
     * @return
     */
    boolean deleteIndex(String index);

    /**
     * 删除文档数据
     * @param index 索引名称
     * @param type 文档类型
     * @param id 数据ID
     * @return
     */
    boolean delSource(String index, String type, String id);

    /**
     * 局部更新数据
     * @param index 索引名称
     * @param type 文档类型
     * @param id 数据ID
     * @param source 新数据
     * @return
     */
    boolean updateSource(String index, String type, String id, Object source);

}
