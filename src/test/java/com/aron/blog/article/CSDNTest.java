package com.aron.blog.article;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @author: Y-Aron
 * @create: 2018-10-16 22:49
 **/
public class CSDNTest implements PageProcessor {
    private Site site = Site.me().setCycleRetryTimes(5).setRetryTimes(5).setSleepTime(500).setTimeOut(3 * 60 * 1000)
            .setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0")
            .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
            .addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
            .setCharset("UTF-8");

    private static final int voteNum = 1000;


    private static final String ARTICLE = "https://blog.csdn.net/\\w+/article/details/\\w+";

    private static final String LIST_URL = "https://www.csdn.net/api/articles.*";

    @Override
    public void process(Page page) {
//        if (page.getUrl().regex(LIST_URL).match()) {
//            String rawText = page.getRawText();
//            JSONObject jsonObject = JSON.parseObject(rawText);
//            JSONArray array = jsonObject.getJSONArray("articles");
//            for (int i = 0; i<array.size(); i++) {
//                JSONObject object = array.getJSONObject(i);
//                System.out.println(array.get(i));
//                page.addTargetRequest("");
//                page.addTargetRequest(object.getString("url"));
//            }
//            page.addTargetRequest("https://www.csdn.net/api/articles?type=more&category=home&shown_offset=0");
//        } else {
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
        Spider.create(new CSDNTest())
                .addUrl("https://www.csdn.net/api/articles?type=more&category=home&shown_offset=0")
                .addPipeline(new ArticlePipeline())
                .thread(5).run();
    }

}
