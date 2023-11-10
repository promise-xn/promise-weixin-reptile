package com.example.weixinapireptile.reptile;


import cn.hutool.core.util.StrUtil;
import com.example.weixinapireptile.common.exceptions.BizException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 文章爬取工具类
 * @author xunuo
 * @date 16:26 2023/11/10
 **/
public class SpiderUtil {

    // 微信公众号文章域名
    private static final String WX_DOMAIN = "http://mp.weixin.qq.com";
    private static final String WX_DOMAINS = "https://mp.weixin.qq.com";
    // 文章返回前端统一key常量
    private static final String KEY_TITLE = "title"; // 文章标题
    private static final String KEY_COVER_URL = "coverLink"; // 文章封面图链接
    private static final String KEY_REFER_NAME = "referName"; // 文章出处作者
    private static final String KEY_REFER_URL = "referLink"; // 文章出处链接

    /**
     * 测试主方法
     */
    public static void main(String args[]) {
//        String url = "https://mp.weixin.qq.com/s/HV5HB5TN-NaGqArMBTn7_A";
//        String resp = getContent(url);
//        if (ObjectUtil.isNotEmpty(resp)) {
//            System.out.println(resp);
//        }

    }

    /**
     * 根据文章链接抓取文章内容
     *
     * @param url 文章链接
     * @return 文章内容
     */
    public static String getContent(String url) {
        // 检测链接是否合法
        boolean msg = checkUrl(url);
        if (!msg) {
            throw new BizException("文章链接不合法！");
        }
        // 请求与响应
        String resp = HttpTool.get(url, getWxHeaderMap());
        if (StrUtil.isEmpty(resp)) {
            throw new BizException("文章获取失败，请检查链接是否正确");
        }
        // 解析
        String wxContent = getWxContent(resp, url);
        if (StrUtil.isEmpty(wxContent)) {
            throw new BizException("解析失败！");
        }
        return wxContent;
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
    public static String getWxContent(String resp, String url) {
        try {
            Document document = Jsoup.parse(resp);
            // 文章出处（作者）
            String referName = document.getElementsByClass("profile_nickname").get(0).html();
            // 文章封面图链接
            String coverUrl = document.select("meta[property=\"og:image\"]").get(0).attr("content");
            // 文章标题
            String title = document.getElementById("activity-name").html();
            // 文章内容
            Element content = document.getElementsByClass("autoTypeSetting24psection").get(0);

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
                // 移除行内样式-------已知这些需要移除，后续有发现再加上
                element.removeAttr("style");
                element.removeAttr("id");
                element.removeAttr("class");
                element.removeAttr("data-darkmode-color-159152540142110");
                element.removeAttr("data-darkmode-original-color-159152540142110");
            }
            return htmlText(elements.get(0).html());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String htmlText(String inputString) {
        // 含html标签的字符串
        String htmlStr = inputString;
        String textStr = "";

        Pattern pattern;
        Matcher matcher;

        try {
            // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
            String regExScript = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
            // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
            String regExStyle = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
            // 定义section标签的正则表达式
            String regExSection = "<section[\\s\\S]*?>[\\s\\S]*?>";
            // 定义div标签的正则表达式
            String regExDiv = "<div[\\s\\S]*?>";
            // 过滤script标签
            pattern = Pattern.compile(regExScript, Pattern.CASE_INSENSITIVE);
            matcher = pattern.matcher(htmlStr);
            htmlStr = matcher.replaceAll("");
            // 过滤style标签
            pattern = Pattern.compile(regExStyle, Pattern.CASE_INSENSITIVE);
            matcher = pattern.matcher(htmlStr);
            htmlStr = matcher.replaceAll("");
            // 过滤section标签
            pattern = Pattern.compile(regExSection, Pattern.CASE_INSENSITIVE);
            matcher = pattern.matcher(htmlStr);
            htmlStr = matcher.replaceAll("");
            // 过滤div标签
            pattern = Pattern.compile(regExDiv, Pattern.CASE_INSENSITIVE);
            matcher = pattern.matcher(htmlStr);
            htmlStr = matcher.replaceAll("");
            textStr = htmlStr;
        } catch (Exception e) {
            System.err.println("Html2Text: " + e.getMessage());
        }
        //剔除
        textStr = textStr.replaceAll("</section>", "");
        textStr = textStr.replaceAll("</div>", "");
        textStr = textStr.replaceAll("data-src", "src");
        textStr = textStr.replaceAll("[ ]+", " ");
        textStr = textStr.replaceAll("(?m)^\\s*$(\\n|\\r\\n)", "");
        // 返回文本字符串
        return textStr;
    }


}
