package com.dawn.html2md.csdn2md;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import cn.wanghaomiao.xpath.exception.XpathSyntaxErrorException;

/**
 * @Author: jiangyj
 * @GitHub: https://github.com/Ruffianjiang
 * @CSDN: https://www.cnblogs.com/lossingdawn
 * @BLOG: https://lossingdanw.top
 * @wxid:
 */
@Service
public class CSDN2mdService {

    // public final static String username = "qqhjqs";
    public final static String HOST_URL = "http://blog.csdn.net/";
    public final static String TOP_XPATH = ".article-item-box,.csdn-tracking-statistics";
    public final static String NOMAIL_QUERY = "#article_list .list_item,.article_item";
    public final static String TARGET_DIR = "F:\\blog\\article";

    public static void main(String[] args) throws IOException, XpathSyntaxErrorException {
        /*-        String convert = convert(new URL("http://blog.csdn.net/qqhjqs/article/details/66474364"));
        System.out.println(convert);*/
        convertAllBlogByUserName("ricohzhanglong", TARGET_DIR);
        // convertAllBlogByUserName("blogdevteam",TARGET_DIR);
    }

    public String convert(URL url) throws IOException {
        Document doc = Jsoup.parse(url, 5000);
        doc.getElementsByTag("script").remove();
        String content = doc.select("#article_content").toString();
        String result = HTML2Md.convertHtml4csdn(content, "utf-8");
        return result;
    }

    public String convert(String html) {
        Document doc = Jsoup.parse(html, "utf-8");
        doc.getElementsByTag("script").remove();
        Elements select = doc.select("#article_content");
        if (select.isEmpty()) {
            String convert = HTML2Md.convert(html, "utf-8");
            return convert;
        } else {
            String content = select.toString();
            String result = HTML2Md.convertHtml4csdn(content, "utf-8");
            return result;
        }
    }

    public void convertAllBlogByUserNames(String username, String filePath) throws IOException {
        convertAllBlogByUserName(username, filePath);
    }

    private static void convertAllBlogByUserName(String username, String filePath) throws IOException {
        // String mdString = "";
        String url = HOST_URL + username + "/article/list/" + 1;
        Document parse = Jsoup.parse(new URL(url), 5000);

        // 以前取总页数的方式，现在有变化，2018-06-13
        /*-
        Element element = parse.select("div#papelist span").get(0);
        String papelistInfo = element.text();
        String totalPage = papelistInfo.split("共")[1].split("页")[0]; // 有点暴力，需注意
        String totalPage = "1";
        int total = Integer.valueOf(totalPage);
        */
        // 新版获取总页数有点坑, 暴力获取页码等
        Elements body = parse.select("body");
        Element pagescript = body.first().children().get(9);
        System.out.println(pagescript.html());

        String pagehtml = pagescript.html();
        String currentPageStr = pagehtml.substring(pagehtml.indexOf("currentPage") + "currentPage =".length(),
                pagehtml.indexOf(";", pagehtml.indexOf("currentPage"))).trim();
        String baseUrl = pagehtml.substring(pagehtml.indexOf("baseUrl") + "baseUrl =".length(),
                pagehtml.indexOf(";", pagehtml.indexOf("baseUrl"))).trim();
        String pageSizeStr = pagehtml.substring(pagehtml.indexOf("pageSize") + "pageSize =".length(),
                pagehtml.indexOf(";", pagehtml.indexOf("pageSize"))).trim();
        String listTotalStr = pagehtml.substring(pagehtml.indexOf("listTotal") + "listTotal =".length(),
                pagehtml.indexOf(";", pagehtml.indexOf("listTotal"))).trim();

        int currentPage = Integer.valueOf(currentPageStr);
        int pageSize = Integer.valueOf(pageSizeStr);
        int listTotal = Integer.valueOf(listTotalStr);

        System.out.println(currentPage);
        System.out.println(baseUrl);
        System.out.println(pageSize);
        System.out.println(listTotal);

        int total = Integer.valueOf((listTotal % pageSize == 0) ? (listTotal / pageSize) : (listTotal / pageSize + 1));
        System.out.println(total);
        Map<String, String> map = new HashMap<>();
        for (int i = 1; i <= total; i++) {
            String listUrl = HOST_URL + username + "/article/list/" + i;
            Document doc = Jsoup.parse(new URL(listUrl), 5000);
            Elements topSelect = doc.select(TOP_XPATH);
            // Elements normalSelect = doc.select(NOMAIL_QUERY);
            for (Element item : topSelect) {
                try {
                    map.put(item.select("h4.text-truncate").first().children().select("a").attr("href"),
                            item.select("h4.text-truncate").first().children().select("a").text());
                } catch (Exception e) {
                }
            }
            /*-            for (Element item : normalSelect) {
                map.put(item.select(".article_title  h1  span  a").get(0).attr("href"),
                        item.select(".article_description").get(0).text());
            }*/
        }
        Set<String> strings = map.keySet();
        for (String item : strings) {
            Document doc = null;
            try {
                doc = Jsoup.parse(new URL(item), 5000);
            } catch (Exception e) {
                System.out.println(e);
                continue;
            }
            BlogModel bm = new BlogModel();
            bm.setTitle(doc.select("h1.title-article").text());
            bm.setDesc(map.get(item));
            bm.setPublishDate(
                    doc.select("#article_details > div.article_manage.clearfix > div.article_r > span.link_postdate")
                            .text());
            Elements select = doc.select("#article_details > div.category.clearfix > div.category_r");
            List<String> cat = new ArrayList<>();
            for (Element ele : select) {
                String text = ele.select("label span").get(0).text();
                int i = text.lastIndexOf("（");
                if (i >= 0) {
                    String substring = text.substring(0, i);
                    cat.add(substring);
                } else {
                    cat.add(text);
                }
            }
            bm.setCategories(cat);
            Elements tagEles = doc.select("#article_details > div.article_manage.clearfix > div.article_l > span > a");
            List<String> tags = new ArrayList<>();
            for (Element ele : tagEles) {
                String text = ele.text();
                tags.add(text);
            }
            bm.setTags(tags);
            doc.getElementsByTag("script").remove();
            bm.setContent(doc.select("#article_content").toString());
            try {
                buildHexo(bm, filePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void buildHexo(BlogModel bm, String filePath) throws IOException {
        System.out.println("保存博文--->[" + bm.getTitle() + "]");
        StringBuffer sb = new StringBuffer();
        sb.append("---");
        sb.append("\r\n");
        sb.append("title: " + bm.getTitle());
        sb.append("\r\n");
        sb.append("date: " + bm.getPublishDate() + ":00");
        sb.append("\r\n");
        sb.append("categories:");
        sb.append("\r\n");
        for (String cat : bm.getCategories()) {
            sb.append("- " + cat);
            sb.append("\r\n");
        }
        sb.append("tags:");
        sb.append("\r\n");
        for (String tag : bm.getTags()) {
            sb.append("- " + tag);
            sb.append("\r\n");
        }
        sb.append("\r\n");
        sb.append("---");
        sb.append("\r\n");
        sb.append(bm.getDesc() == null ? "" : bm.getDesc());
        sb.append("\r\n");
        sb.append("<!--more-->");
        sb.append("\r\n");
        sb.append("\r\n");
        sb.append(HTML2Md.convertHtml4csdn(bm.getContent(), "UTF-8"));

        IOUtils.write(sb.toString(),
                new FileOutputStream(new File(filePath + File.separator + File.separator + bm.getTitle() + ".md")));
    }

}
