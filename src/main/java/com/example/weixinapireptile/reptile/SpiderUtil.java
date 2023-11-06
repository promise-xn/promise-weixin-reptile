package com.example.weixinapireptile.reptile;

/**
 * @author xunuo
 * @description: TODO
 * @datetime 2023年 10月 26日 11:08
 */

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;


/**
 * 文章爬取工具类
 *
 * @author ZhangYuanqiang
 * @since 2020/01/04
 */
public class SpiderUtil {

    // 微信公众号文章域名
    private static final String WX_DOMAIN = "http://mp.weixin.qq.com";
    private static final String WX_DOMAINS = "https://mp.weixin.qq.com";
    // 文章返回前端统一key常量
    private static final String KEY_TITLE = "title"; // 文章标题
    private static final String KEY_COVER_URL = "coverLink"; // 文章封面图链接
    private static final String KEY_REFER_NAME = "referName"; // 文章出处作者
    private static final String KEY_REFER_URL = "referLink"; // 文章出处链接
    private static final String KEY_TAGS = "tags"; // 文章内容
    private static final String KEY_NAME = "name"; // 标签名称
    private static final String KEY_TEXT = "text"; // 文本信息
    private static final String KEY_HREF = "href"; // a标签链接
    private static final String KEY_IMAGE = "image"; // a标签链接

    /**
     * 测试主方法
     */
    public static void main(String args[]) {
        String url = "https://mp.weixin.qq.com/s/K0AnpxKjGmPv5Dd9BDOjMg";
        Resp<JSONObject> resp = getActicle(url);
        if (resp.isSuccess()) {
            System.out.println(resp.getBody());
        } else {
            System.out.println(resp.getMsg());
        }
    }

    /**
     * 根据文章链接抓取文章内容
     *
     * @param url 文章链接
     * @return 文章内容
     */
    public static Resp<JSONObject> getActicle(String url) {
        // 检测链接是否合法
        boolean msg = checkUrl(url);
        if (msg != true) {
            return Resp.error("请输入正确的微信公众号文章链接");
        }
        // 请求与响应
        String resp = HttpTool.get(url, getWxHeaderMap());
        if (resp == null || resp.trim().length() == 0) {
            return Resp.error("文章获取失败，请检查链接是否正确");
        }
        // 解析
        Resp<JSONObject> acticleResp = getWxActicleContent(resp, url);
        if (acticleResp.isError()) {
            return Resp.error(acticleResp.getMsg());
        }
        return acticleResp;
    }

    /**
     * 检测文章链接是否合法
     */
    public static boolean checkUrl(String url) {
        if (url == null) {
            return false;
        }
        if (url.startsWith(WX_DOMAIN) || url.startsWith(WX_DOMAINS)) {
            return true;
        }
        return false;
    }


    /**
     * 微信公众号请求头设置
     */
    public static Map<String, String> getWxHeaderMap() {
        Map<String, String> map = new HashMap<>(new LinkedHashMap<>());
        map.put("Accept", "text/html, application/xhtml+xml, image/jxr, */*");
        map.put("Accept-Encoding", "gzip, deflate");
        map.put("Accept-Language", "zh-Hans-CN, zh-Hans; q=0.8, en-US; q=0.5, en; q=0.3");
        map.put("Host", "mp.weixin.qq.com");
        map.put("If-Modified-Since", "Sat, 04 Jan 2020 12:23:43 GMT");
        map.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko");
        return map;
    }


    /**
     * 解析微信公众号文章
     *
     * @param resp 请求文章响应
     * @param url  文章链接
     * @return 文章信息
     */
    public static Resp<JSONObject> getWxActicleContent(String resp, String url) {
        try {
            Document document = Jsoup.parse(resp);
            // 文章出处（作者）
            String referName = document.getElementsByClass("profile_nickname").get(0).html();
            // 文章封面图链接
            String coverUrl = document.select("meta[property=\"og:image\"]").get(0).attr("content");
            // 文章标题
            String title = document.getElementById("activity-name").html();
            // 文章内容
//            Element content = document.getElementsByClass("rich_media_area_primary_inner").get(0);
            Element content = document.getElementsByClass("autoTypeSetting24psection").get(0);
//            String primaryInner = document.getElementsByClass("rich_media_area_primary_inner").get(0).html();
            JSONObject json = new JSONObject(new LinkedHashMap<>());
            json.put(KEY_TITLE, title);
            json.put(KEY_COVER_URL, coverUrl);
            json.put(KEY_REFER_NAME, referName);
            json.put(KEY_REFER_URL, url);
            Elements elements = content.select("*");
            for (Element element : elements) {
                // 忽略标签
                List<String> array = new ArrayList();
                array.add("br");
                array.add("iframe");
                array.add("canvas");
                array.add("svg");
                array.add("mp-style-type");
                array.add("li");
                array.add("ul");
                array.add("label");
                array.add("em");
                if (array.contains(element.tagName())) {
                    element.remove();
                }
                // 相对路径图片处理
                else if ("img".equals(element.tagName())) {
                    element.removeAttr("data-type");
                    element.removeAttr("data-ratio");
                    element.removeAttr("data-s");
                    element.removeAttr("data-w");
                    element.removeAttr("class");
                    element.removeAttr("data-backw");
                    element.removeAttr("data-backh");
                }
                // 移除行内样式
                element.removeAttr("style");
                element.removeAttr("id");
                element.removeAttr("class");
                element.removeAttr("data-darkmode-color-159152540142110");
                element.removeAttr("data-darkmode-original-color-159152540142110");
            }
//            String html = elements.html();
//            String html2Text1 = Html2Text(html);
//            System.out.println(Html2Text(document.getElementsByClass("rich_media_area_primary_inner").get(0).html()));
//            System.out.println(Html2Text(document.getElementsByClass("autoTypeSetting24psection").get(0).html()));
            String html = elements.get(0).html();
            System.out.println(Html2Text(html));
//            for (Element element : sections) {
//                if (element.children().isEmpty()) {
////                    getChildTag(element, tags);
////                    System.out.println(element.html());
////                    if (element.tagName().equals("img")){
////                        System.out.println(element.attr("abs:data-src"));
////                    }
////                    if (element.tagName().equals("span")){
////                        System.out.println(element.text());
////                    }
////                    if (element.tagName().equals("p")){
////                        System.out.println(element.html());
////                    }
//                }
//
//            }
//            json.put(KEY_TAGS, html2Text);
//            json.put(KEY_TAGS,Html2Text(primaryInner));
            return Resp.success(json);
        } catch (Exception e) {
            e.printStackTrace();
            return Resp.error("文章解析失败");
        }
    }

    public static void getChildTag(Element element, JSONArray tags) {
        JSONObject tag = new JSONObject(new LinkedHashMap<>());
        String tagName = element.tagName();
        tag.put(KEY_NAME, tagName);
        switch (tagName) {
            case "span": {
                tag.put(KEY_TEXT, element.text());
                tags.add(tag);
                break;
            }
            case "img": {

                tag.put(KEY_IMAGE, element.attr("abs:data-src"));
                System.out.println(element.attr("abs:data-src"));
                tags.add(tag);
                break;
            }
            case "a": {
                tag.put(KEY_HREF, element.attr("href"));
                tag.put(KEY_TEXT, element.attr("textvalue"));
                tags.add(tag);
                break;
            }
//            case "br": {
//                tags.add(tag);
//                break;
//            }
            case "p": {
                tag.put(KEY_TEXT, element.text());
                System.out.println(element.html());
                tags.add(tag);
                break;
            }
            default:
                break;
        }
    }

    public static String Html2Text(String inputString) {
        String htmlStr = inputString; // 含html标签的字符串
        String textStr = "";
//        java.util.regex.Pattern p_script;
//        java.util.regex.Matcher m_script;
//        java.util.regex.Pattern p_style;
//        java.util.regex.Matcher m_style;
//        java.util.regex.Pattern p_html;
//        java.util.regex.Matcher m_html;

        java.util.regex.Pattern pattern;
        java.util.regex.Matcher matcher;
        try {
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
            String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
            String regEx_section = "<section[\\s\\S]*?>[\\s\\S]*?>"; // 定义HTML标签的正则表达式
//            String regEx_div = "<div[\\s\\S]*?>[\\s\\S]*?>\n"; // 定义HTML标签的正则表达式
//            String regEx_div = "<div\\b[^>]*>"; // 定义HTML标签的正则表达式
            String regEx_div = "<div[\\s\\S]*?>"; // 定义HTML标签的正则表达式
            pattern = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            matcher = pattern.matcher(htmlStr);
            htmlStr = matcher.replaceAll(""); // 过滤script标签
            pattern = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            matcher = pattern.matcher(htmlStr);
            htmlStr = matcher.replaceAll(""); // 过滤style标签
            pattern = Pattern.compile(regEx_section, Pattern.CASE_INSENSITIVE);
            matcher = pattern.matcher(htmlStr);
            htmlStr = matcher.replaceAll(""); // 过滤html标签
            pattern = Pattern.compile(regEx_div, Pattern.CASE_INSENSITIVE);
            matcher = pattern.matcher(htmlStr);
            htmlStr = matcher.replaceAll(""); // 过滤html标签
            textStr = htmlStr;
        } catch (Exception e) {
            System.err.println("Html2Text: " + e.getMessage());
        }
        //剔除空格行
        textStr = textStr.replaceAll("</section>", "");
        textStr = textStr.replaceAll("</div>", "");
        textStr = textStr.replaceAll("data-src", "src");
        textStr = textStr.replaceAll("[ ]+", " ");
        textStr = textStr.replaceAll("(?m)^\\s*$(\\n|\\r\\n)", "");


        return textStr;// 返回文本字符串
    }


}
