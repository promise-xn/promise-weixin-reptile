package com.example.weixinapireptile.common.okhttp;

import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 静态化一个okhttp实例
 * @author zsq
 * @version 1.0
 * @date 2021/3/31 10:58
 */
@Component
public class MyOkHttpClient {

    /**okhttp实例**/
    public static OkHttpClient client;

    private static String referer = "https://mp.weixin.qq.com/";
    private static String userAgent = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.190 Safari/537.36";
    private static String xRequestedWith = "XMLHttpRequest";

    static {
        //初始化一个okhttp实例
        client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(5, 20, TimeUnit.SECONDS))
                .readTimeout(15, TimeUnit.SECONDS)


                //管理cookie
                .cookieJar(new MyCookieStore())
                //添加请求头
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        //添加请求头
                        Request request = chain.request().newBuilder()
                                .addHeader("referer", referer)
                                .addHeader("userAgent", userAgent)
                                .addHeader("xRequestedWith", xRequestedWith)
                                .build();
//                        System.out.println("client----------"+ request.toString());
                        return chain.proceed(request);
                    }
                })
                .build();

    }

}
