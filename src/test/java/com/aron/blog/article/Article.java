package com.aron.blog.article;


import lombok.Data;
import lombok.ToString;

/**
 * @author: Y-Aron
 * @create: 2018-10-16 21:49
 **/
@Data
@ToString
public class Article {

    private String title;
    private String module;
    private String readCount;
    private String author;
    private String content;

}