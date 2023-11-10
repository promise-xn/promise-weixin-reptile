package com.example.weixinapireptile.common.utils;

import com.example.weixinapireptile.common.okhttp.MyOkHttpClient;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zsq
 * @version 1.0
 * @date 2021/3/31 11:06
 */
@Slf4j
public class HttpUtils {

    /**okhttp 实例**/
    private static OkHttpClient client = MyOkHttpClient.client;

    /**解析键值对正则**/
    public static final Pattern PAT_KEY_VALUE = Pattern.compile("[\\w]*=[\\w,/]*");


    /**
     * get请求
     * @param url
     * @param params
     * @return
     */
    public static String doGet(String url, Map<String, String> params) {
        Request.Builder reqBuild = new Request.Builder();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url)
                .newBuilder();
        if (params != null) {
            params.forEach(urlBuilder::addQueryParameter);
        }

        reqBuild.url(urlBuilder.build());
        Request request = reqBuild.build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            System.out.println("request------------"+request.toString());
            if (!response.isSuccessful()) {
                throw new RuntimeException("请求不成功" + response.toString());
            }
            return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * post表单提交
     * @return
     */
    public static String doPost(String url, Map<String, String> params) {
        FormBody.Builder formBuilder = new FormBody.Builder();
        if (params != null) {
            params.forEach(formBuilder::addEncoded);
        }

        Request request = new Request.Builder()
                .url(url)
                .post(formBuilder.build())
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new RuntimeException("请求不成功" + response.toString());
            }
            return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * get请求，返回流
     * @param url
     * @param params
     * @return
     */
    public static InputStream doGetStream(String url, Map<String, String> params) {
        Request.Builder reqBuild = new Request.Builder();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url)
                .newBuilder();
        if (params != null) {
            params.forEach(urlBuilder::addQueryParameter);
        }

        reqBuild.url(urlBuilder.build());
        Request request = reqBuild.build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new RuntimeException("请求不成功" + response.toString());
            }

            if (response.body() == null) {
                throw new RuntimeException("返回内容body为空");
            }

            return response.body().byteStream();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 字符解析成键值对参数
     * @return
     */
    public static Map<String, String> parseQueryParams (String path) {
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isNotBlank(path)) {
            Matcher matcher = PAT_KEY_VALUE.matcher(path);
            while (matcher.find()) {
                String group = matcher.group(0);
                String[] split = group.split("=");
                if (split.length > 1) {
                    map.put(split[0], split[1]);
                } else {
                    map.put(split[0], "");
                }
            }
        }

        return map;
    }

}
