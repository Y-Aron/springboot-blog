package com.aron.blog.article;

import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @author: Y-Aron
 * @create: 2018-10-17 00:01
 **/
public class BlogTest implements PageProcessor {
    private Site site = Site.me().setCycleRetryTimes(5).setRetryTimes(5).setSleepTime(100).setTimeOut(3 * 60 * 1000)
            .setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0")
            .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
            .addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")

            .setCharset("UTF-8");

    private static final String URL_POST = "https://www.cnblogs.com/\\w+/p/\\d+.html";

    private static final String LIST_URL = "https://www.cnblogs.com/sitehome/p/\\d+";

    private static int count;

    @Override
    public void process(Page page) {
        if (page.getUrl().regex(LIST_URL).match()) {
            page.addTargetRequests(page.getHtml()
                    .xpath("//[@id=post_list]").links().regex(URL_POST).all());
            System.out.println(page.getHtml().links().regex(LIST_URL));
            page.addTargetRequests(page.getHtml().links().regex(LIST_URL).all());
        } else {

            Article article = new Article();
            // 文章标题
            article.setTitle(page.getHtml()
                    .xpath("//a[@id=cb_post_title_url]/text()").toString());

            if (StringUtils.isBlank(article.getTitle())) {
                page.setSkip(true);
            }

            // 文章内容
            article.setContent(page.getHtml()
                    .xpath("//div[@id=cnblogs_post_body]").toString());

            // 文章作者
            article.setAuthor(page.getHtml()
                    .xpath("//a[@id='Header1_HeaderTitle']/text()").toString());

            page.putField("articles", article);

            count ++;
            System.out.println(count);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new BlogTest())
                .addUrl("https://www.cnblogs.com/sitehome/p/1")
                .addPipeline(new ArticlePipeline()).run();
    }
}
