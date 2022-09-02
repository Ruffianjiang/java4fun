package com.dawn.html2md.csdn2md;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.seimicrawler.xpath.exception.XpathSyntaxErrorException;
import org.springframework.stereotype.Service;


/**
 * @Author: jiangyj
 * @GitHub: https://github.com/Ruffianjiang
 * @CSDN: https://www.cnblogs.com/lossingdawn
 * @BLOG: https://lossingdanw.top
 * @wxid: 
 */
@Service
public class CNBlog2mdService {

    // public final static String username = "qqhjqs";
    public final static String HOST_URL = "http://www.cnblogs.com/";
    public final static String TOP_XPATH = "div.day";
    public final static String TARGET_DIR = "F:\\blog\\article\\lossingdawn\\";

    public static void main(String[] args) throws IOException, XpathSyntaxErrorException {
        /*-        
        String convert = convert(new URL("http://blog.csdn.net/qqhjqs/article/details/66474364"));
        System.out.println(convert);*/
        convertAllBlogByUserName();
    }

    public String convert(URL url) throws IOException {
        Document doc = Jsoup.parse(url, 5000);
        doc.getElementsByTag("script").remove();
        String content = doc.select("#article_content").toString();
        return HTML2Md.convertHtml4csdn(content, "utf-8");
    }

    public String convert(String html) {
        Document doc = Jsoup.parse(html, "utf-8");
        doc.getElementsByTag("script").remove();
        Elements select = doc.select("#article_content");
        if (select.isEmpty()) {
            return HTML2Md.convert(html, "utf-8");
        } else {
            String content = select.toString();
            return HTML2Md.convertHtml4csdn(content, "utf-8");
        }
    }

    private static void convertAllBlogByUserName() throws IOException {
        // String mdString = "";
        String url = HOST_URL + "lossingdawn" + "/";
        Document parse = Jsoup.parse(new URL(url), 5000);
        Element element = parse.select("div#blog_stats").get(0);
        // 有点暴力，需注意

        String papelistInfo = element.text();
        String totalSizeStr = papelistInfo
                .substring(papelistInfo.indexOf("随笔") + "随笔 -".length(), papelistInfo.indexOf("文章")).replaceAll("\\D", "");
        int totalSize = Integer.parseInt(totalSizeStr);
        int total = (totalSize % 10 == 0) ? (totalSize / 10) : (totalSize / 10 + 1);
        System.out.println(total);
        Map<String, String> map = new HashMap<>();
        for (int i = 1; i <= total; i++) {
            String listUrl = HOST_URL + "lossingdawn" + "/default.html?page=" + i;
            Document doc = Jsoup.parse(new URL(listUrl), 5000);
            Elements topSelect = doc.select(TOP_XPATH);
            for (Element item : topSelect) {
                map.put(Objects.requireNonNull(item.select("div.postTitle").first()).children().select("a").attr("href"),
                        Objects.requireNonNull(item.select("div.c_b_p_desc").first()).text());
            }
        }
        Set<String> strings = map.keySet();
        for (String item : strings) {

            Document doc = Jsoup.parse(new URL(item), 5000);
            BlogModel bm = new BlogModel();
            bm.setTitle(doc.select("a#cb_post_title_url").text());
            bm.setDesc(map.get(item));
            bm.setPublishDate(doc.select("span#post-date").text() + ":00");

            Elements categor = Objects.requireNonNull(doc.select("div#BlogPostCategory").first()).children().select("a");
            List<String> cat = new ArrayList<>();
            for (Element ele : categor) {
                cat.add(ele.text());
            }
            bm.setCategories(cat);

            Elements tagEles = Objects.requireNonNull(doc.select("div#EntryTag").first()).children().select("a");
            List<String> tags = new ArrayList<>();
            for (Element ele : tagEles) {
                String text = ele.text();
                tags.add(text);
            }
            bm.setTags(tags);
            
//            doc.getElementsByTag("script").remove();
            bm.setContent(doc.select("#cnblogs_post_body").toString() + doc.select("#MySignature").toString());
            try {
                buildHexo(bm);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void buildHexo(BlogModel bm) throws IOException {
        System.out.println("保存博文--->[" + bm.getTitle() + "]");
        StringBuilder sb = new StringBuilder();
        sb.append("---");
        sb.append("\r\n");
        sb.append("title: ").append(bm.getTitle());
        sb.append("\r\n");
        sb.append("date: ").append(bm.getPublishDate());
        sb.append("\r\n");
        sb.append("categories:");
        sb.append("\r\n");
        for (String cat : bm.getCategories()) {
            sb.append("- ").append(cat);
            sb.append("\r\n");
        }
        sb.append("tags:");
        sb.append("\r\n");
        for (String tag : bm.getTags()) {
            sb.append("- ").append(tag);
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
        sb.append(HTML2Md.convertHtml(bm.getContent(), "UTF-8"));
        IOUtils.write(sb.toString(),
                Files.newOutputStream(new File(TARGET_DIR + File.separator + bm.getTitle() + ".md").toPath()),
                Charset.defaultCharset());
    }

}
