package com.aron.blog.article;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Map;

/**
 * @author: Y-Aron
 * @create: 2018-10-16 21:54
 **/
public class ArticlePipeline implements Pipeline {



    @Override
    public void process(ResultItems resultItems, Task task) {
        for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
            if (entry.getKey().contains("articles")) {
                Article article = (Article) entry.getValue();
//                esRepository.setSource("blog", "articles", article);
            }

        }
    }
}
