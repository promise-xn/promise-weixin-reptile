package com.example.weixinapireptile.api;

import com.example.weixinapireptile.model.WxResultBody;
import com.example.weixinapireptile.common.utils.HttpUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.StringUtils;
import com.example.weixinapireptile.common.enums.WxResultStatus;
import com.example.weixinapireptile.common.exceptions.WxApiException;
import com.example.weixinapireptile.model.Article;
import com.example.weixinapireptile.model.BizData;
import com.example.weixinapireptile.common.okhttp.MyCookieStore;
import com.example.weixinapireptile.common.utils.JsonUtils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信api封装
 * @author zsq
 * @version 1.0
 * @date 2021/3/31 11:13
 */
public class WeiXinApi {

    /**接口地址**/
    public static final Map<String, String> URL_MAP = new HashMap<>();

    static {
        //初始化登录cookie
        URL_MAP.put("startLogin", "https://mp.weixin.qq.com/cgi-bin/bizlogin?action=startlogin");

        //二维码图片接口
        URL_MAP.put("getqrcode", "https://mp.weixin.qq.com/cgi-bin/scanloginqrcode");

        //二维码扫描状态接口
        URL_MAP.put("qrcodeAsk", "https://mp.weixin.qq.com/cgi-bin/scanloginqrcode");

        //扫码确认后，登录接口
        URL_MAP.put("bizlogin", "https://mp.weixin.qq.com/cgi-bin/bizlogin?action=login");

        //扫码确认后，登录接口
        URL_MAP.put("searchbiz", "https://mp.weixin.qq.com/cgi-bin/searchbiz");

        //扫码确认后，登录接口
        URL_MAP.put("findListEx", "https://mp.weixin.qq.com/cgi-bin/appmsg");
    }

    /**
     * POST请求开始登录接口，初始化cookie
     * @param sessionid 时间戳，加两位随机数
     * @return
     */
    public static WxResultBody startLogin(String sessionid) {
        if (StringUtils.isBlank(sessionid)) {
            sessionid = "" + System.currentTimeMillis() + (int)(Math.random()*100);
        }

        Map<String, String> params = new HashMap<>(8);
        params.put("sessionid", sessionid);
        params.put("userlang", "zh_CN");
        params.put("redirect_url", "");
        params.put("login_type", "3");
        params.put("token", "");
        params.put("lang", "zh_CN");
        params.put("f", "json");
        params.put("ajax", "1");

        WxResultBody wxResultBody = parseWxResultBody(HttpUtils.doPost(URL_MAP.get("startLogin"), params));

        return wxResultBody;
    }

    /**
     * 获取二维码图片 字节输出流
     * @return
     */
    public static InputStream getQRCode() {
        Map<String, String> params = new HashMap<>(2);
        params.put("action", "getqrcode");
        params.put("random", System.currentTimeMillis() + "");
        InputStream inputStream = HttpUtils.doGetStream(URL_MAP.get("getqrcode"), params);

        return inputStream;
    }


    /**
     * GET 轮询二维码状态接口
     * @return
     */
    public static WxResultBody askQRCode() {
        Map<String, String> params = new HashMap<>(5);
        params.put("action", "ask");
        params.put("token", "");
        params.put("lang", "zh_CN");
        params.put("f", "json");
        params.put("ajax", "1");

        WxResultBody wxResultBody = parseWxResultBody(HttpUtils.doGet(URL_MAP.get("qrcodeAsk"), params));

        return wxResultBody;
    }

    /**
     * 扫码确认后，登录接口
     * @return
     */
    public static WxResultBody bizlogin() {
        Map<String, String> params = new HashMap<>(10);
        params.put("userlang", "zh_CN");
        params.put("redirect_url", "");
        params.put("cookie_forbidden", "0");
        params.put("cookie_cleaned", "0");
        params.put("plugin_used", "0");
        params.put("login_type", "3");
        params.put("token", "");
        params.put("lang", "zh_CN");
        params.put("f", "json");
        params.put("ajax", "1");

        WxResultBody wxResultBody = parseWxResultBody(HttpUtils.doPost(URL_MAP.get("bizlogin"), params));

        return wxResultBody;
    }


    /**
     * 搜索公众号
     * @return
     */
    public static WxResultBody<List<BizData>> searchBiz(String keyword){
        Map<String, String> params = new HashMap<>(10);
        params.put("action", "search_biz");
        params.put("begin", "0");
        params.put("count", "1");
        params.put("query", keyword);
        params.put("token", MyCookieStore.getToken());
        params.put("lang", "zh_CN");
        params.put("f", "json");
        params.put("ajax", "1");
        String searchbiz = HttpUtils.doGet(URL_MAP.get("searchbiz"), params);
        System.out.println("文章列表数据-----------------"+searchbiz);
        WxResultBody<List<BizData>> wxResultBody = parseWxResultBody(searchbiz,
                new TypeReference<WxResultBody<List<BizData>>>() {}
        );

        return wxResultBody;
    }

    /**
     * 搜索公众号的文章
     * @return
     */
    public static WxResultBody<List<Article>> findExList(String fakeid){
        Map<String, String> params = new HashMap<>(10);
        params.put("action", "list_ex");
        params.put("begin", "0");
        params.put("count", "1");
        params.put("fakeid", fakeid);
        params.put("token", MyCookieStore.getToken());
        params.put("type", "9");
        params.put("query", "");
        params.put("lang", "zh_CN");
        params.put("f", "json");
        params.put("ajax", "1");
        String listEx = HttpUtils.doGet(URL_MAP.get("findListEx"), params);
//        System.out.println("文章主要信息数据--------------------"+listEx);
//        WxResultBody<List<Article>> wxResultBody = parseWxResultBody(HttpUtils.doGet(URL_MAP.get("findListEx"), params),
//                new TypeReference<WxResultBody<List<Article>>>() {}
//        );

        WxResultBody<List<Article>> wxResultBody = parseWxResultBody(listEx,
            new TypeReference<WxResultBody<List<Article>>>() {}
        );
        return wxResultBody;
    }


    /**
     * 转成java bean
     * @param jsonRes json结果字符串
     * @return
     */
    public static WxResultBody parseWxResultBody(String jsonRes) {
        WxResultBody wxResultBody = JsonUtils.jsonToObj(jsonRes, WxResultBody.class);

        checkIsSuccess(wxResultBody);

        return wxResultBody;
    }

    /**
     * 转成java bean
     * @param jsonRes json结果字符串
     * @return
     */
    public static <T> T parseWxResultBody(String jsonRes, TypeReference<T> typeReference) {
        T wxResultBody = JsonUtils.jsonToObj(jsonRes, typeReference);
        checkIsSuccess((WxResultBody) wxResultBody);
        return wxResultBody;
    }

    public static void checkIsSuccess(WxResultBody wxResultBody) {
        // 判断是否请求成功
        if (wxResultBody == null) {
            throw new WxApiException(WxResultStatus.FAIL_NULL_RES);
        }

        if (wxResultBody.getBase_resp().getRet() == null
                || wxResultBody.getBase_resp().getRet() != 0) {
            throw new WxApiException(WxResultStatus.FAIL_STATUS);
        }
    }

}
