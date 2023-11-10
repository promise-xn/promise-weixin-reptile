package com.example.weixinapireptile.common.okhttp;

import com.example.weixinapireptile.api.RedisCache;
import com.example.weixinapireptile.common.utils.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * cookie 管理
 * @author xunuo
 * @date 10:58 2023/11/8
 **/
@Component
@Slf4j
public class MyCookieStore implements CookieJar {


    /**登录后拿到的token**/
    private static String TOKEN = "";

    /**管理cookie**/
    private static ConcurrentHashMap<String, ConcurrentHashMap<String, Cookie>> cookieStore = new ConcurrentHashMap<>();


    @Override
    public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
        ConcurrentHashMap<String, Cookie> cookieMap = cookieStore.get(httpUrl.host());
        if (cookieMap == null) {
            cookieMap = new ConcurrentHashMap<>();
            cookieStore.put(httpUrl.host(), cookieMap);
        }
        for (Cookie cookie : list) {
            cookieMap.put(cookie.name(), cookie);
        }

        if (list != null && list.size() > 0) {
            // 在这里将cookie持久化入redis
            // 存键值对
            RedisCache redisCache = BeanUtils.getBean(RedisCache.class);
            Map<String, String> cookie = redisCache.getCacheMap("cookie");
            cookieMap.forEach((k,v)->{
                redisCache.setCacheMapValue("cookie",k,encodeCookie(new SerializableOkHttpCookies(v)));
            });
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl httpUrl) {
        List<Cookie> cookies = new ArrayList<>();
        System.out.println("httpUrl host:----------"+httpUrl.host() );
        Map<String, Cookie> cookieMap = cookieStore.get(httpUrl.host());
        // 加载获取cookie
        // 持久化后，在getImage获取二维码的接口处，直接将redis缓存的cookie干掉
        // 判断redis中是否有缓存数据，如果有直接取，如果没有获取cookie，放入cookie
        RedisCache redisCache = BeanUtils.getBean(RedisCache.class);
        boolean hasKey = redisCache.hasKey("cookie");
        if (!hasKey){
            if (cookieMap != null) {
                cookieMap.forEach((name, cookie)->{
                    cookies.add(cookie);
                });
            }
        }else {
            Map<String, String> cacheMap = redisCache.getCacheMap("cookie");
            cacheMap.forEach((k,v)->{
                Cookie cookie = decodeCookie(v);
                cookies.add(cookie);
            });
        }
        return cookies;
    }

    public static void setToken(String token) {
        TOKEN = token;
    }

    public static String getToken() {
        return TOKEN;
    }



    /**
     * cookies 序列化成 string
     *
     * @param cookie 要序列化的cookie
     * @return 序列化之后的string
     */
    protected String encodeCookie(SerializableOkHttpCookies cookie) {
        if (cookie == null) {

            return null;
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(os);
            outputStream.writeObject(cookie);
        } catch (IOException e) {
            return null;
        }

        return byteArrayToHexString(os.toByteArray());
    }

    /**
     * 将字符串反序列化成cookies
     *
     * @param cookieString cookies string
     * @return cookie object
     */
    protected Cookie decodeCookie(String cookieString) {
        byte[] bytes = hexStringToByteArray(cookieString);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        Cookie cookie = null;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            cookie = ((SerializableOkHttpCookies) objectInputStream.readObject()).getCookies();
        } catch (IOException e) {
            e.printStackTrace();
            log.info(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }
        return cookie;
    }

    /**
     * 二进制数组转十六进制字符串
     *
     * @param bytes byte array to be converted
     * @return string containing hex values
     */
    protected String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte element : bytes) {
            int v = element & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase(Locale.US);
    }

    /**
     * 十六进制字符串转二进制数组
     *
     * @param hexString string of hex-encoded values
     * @return decoded byte array
     */
    protected byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }
}
