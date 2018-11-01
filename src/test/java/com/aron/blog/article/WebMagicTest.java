package com.aron.blog.article;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @author: Y-Aron
 * @create: 2018-10-16 19:03
 **/
public class WebMagicTest implements PageProcessor {

    private Site site = Site.me().setSleepTime(100).setTimeOut(10000).setUseGzip(true);

    private static final String URL_LIST = "https://www.csdn.net/";

    private static final String URL_POST = "https://blog.csdn.net/\\w+/article/details/\\d+";


    @Override
    public void process(Page page) {

//        if (page.getUrl().regex(URL_LIST).match()) {
//            page.addTargetRequests(page.getHtml()
//                    .xpath("//[@id=feedlist_id]").links().regex(URL_POST).all());
////            page.addTargetRequests(page.getHtml().links().regex(URL_LIST).all());
//        } else {
//
//            Article article = new Article();
//            article.setTitle(page.getHtml().xpath("//h1[@class=title-article]/text()").toString());
//            article.setModule(page.getHtml().xpath("//div[@class=article-bar-top]" +
//                    "/a[@class=follow-nickName]/text()").toString());
//            article.setReadCount(page.getHtml().xpath("//div[@class=article-bar-top]" +
//                    "/span[@class=read-count]/text()").toString());
//            page.putField("articles", article);
//        }


    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new WebMagicTest())
                .addUrl("https://www.csdn.net/")
                .addPipeline(new ArticlePipeline())
                .thread(3).run();

    }
}
