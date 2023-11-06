package com.example.weixinapireptile.okhttp;

import com.example.weixinapireptile.api.RedisCache;
import com.example.weixinapireptile.utils.BeanUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import com.example.weixinapireptile.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * cookie管理
 * @author zsq
 * @version 1.0
 * @date 2021/3/31 16:17
 */
@Component
public class MyCookieStore implements CookieJar {


    /**打印日志用**/
    private static volatile boolean debug = false;

    /**登录后拿到的token**/
    private static String TOKEN = "";

    /**管理cookie**/
    private static ConcurrentHashMap<String, ConcurrentHashMap<String, Cookie>> cookieStore = new ConcurrentHashMap<>();

    /**token资源文件位置**/
    public static final String TOKEN_FILE_PATH = "/store/token.txt";

    /**cookie资源文件位置**/
    public static final String COOKIE_FILE_PATH = "/store/cookie.txt";


    @Override
    public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
        log("-----保存cookie");

        //保存cookie
        log("httpUrl host: " + httpUrl.host());

        ConcurrentHashMap<String, Cookie> cookieMap = cookieStore.get(httpUrl.host());
        if (cookieMap == null) {
            cookieMap = new ConcurrentHashMap<>();
            cookieStore.put(httpUrl.host(), cookieMap);
        }
        for (Cookie cookie : list) {
            cookieMap.put(cookie.name(), cookie);
            System.out.println("cookie:----------"+cookie.toString());
        }

        if (list != null && list.size() > 0) {
            //存到本地，需要自己写持久化的实现，可用redis，或本地
            // TODO 在这里将cookie持久化入redis
            // TODO 存键值对
            RedisCache redisCache = BeanUtils.getBean(RedisCache.class);
            Map<String, String> cookie = redisCache.getCacheMap("cookie");
            cookieMap.forEach((k,v)->{
                redisCache.setCacheMapValue("cookie",k,encodeCookie(new SerializableOkHttpCookies(v)));
            });
//            Set<String> strCookie = new HashSet<>();
//            for (Cookie cookie : list) {
//                String s = encodeCookie(new SerializableOkHttpCookies(cookie));
//                strCookie.add(s);
//            }
//            Set<String> cookie = redisCache.getCacheSet("cookie");
//            if (cookie.size()>0){
//                cookie.addAll(strCookie);
//                redisCache.setCacheSet("cookie",cookie);
//            }else {
//                redisCache.setCacheSet("cookie",strCookie);
//            }
        }
//        System.out.println("保存到cookie:----------"+cookieStore.toString());
        log("-----保存到cookie：" + cookieStore.toString());
        log("------------------------：");
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl httpUrl) {
        log("---加载 cookie");
        List<Cookie> cookies = new ArrayList<>();
        System.out.println("httpUrl host:----------"+httpUrl.host() );
        Map<String, Cookie> cookieMap = cookieStore.get(httpUrl.host());
        // TODO 加载获取cookie
        // TODO 持久化后，在getImage获取二维码的接口处，直接将redis缓存的cookie干掉
        // TODO 判断redis中是否有缓存数据，如果有直接取，如果没有获取cookie，放入cookie
        RedisCache redisCache = BeanUtils.getBean(RedisCache.class);
        boolean hasKey = redisCache.hasKey("cookie");
        if (!hasKey){
            if (cookieMap != null) {
                cookieMap.forEach((name, cookie)->{
                    log("--cookie:" + name + "=" + cookie.value());
                    cookies.add(cookie);
                });
            }
        }else {
            Set<String> cacheList = redisCache.getCacheSet("cookie");
            Map<String,Cookie> cacheMap = new HashMap<>();
            for (String s : cacheList) {
                Cookie cookie = decodeCookie(s);
                System.out.println("redis_cookie----------------"+cookie.toString());
                cacheMap.put(cookie.name(), cookie);
            }
//            Map<String, String> cacheMap = redisCache.getCacheMap("cookie");
//            cacheMap.forEach((name, cookie)->{
//                cookies.add(decodeCookie(cookie));
//            });
        }

        log("---load cookie");
        return cookies;
    }

    public static void setToken(String token) {
        TOKEN = token;
    }

    public static String getToken() {
        return TOKEN;
    }

    /**
     * 保存cookie
     * @param resPath 资源文件目录
     */
    public static void saveCookie (String resPath, ConcurrentHashMap<String, ConcurrentHashMap<String, Cookie>> cookieMap) {

    }

    /**
     * 保存到文件
     * @param resPath 资源文件目录
     */
    public static void saveToFile (String resPath, String str) {
        URL resource = MyCookieStore.class.getResource(resPath);
        try {
            IOUtils.write(str.getBytes(), new FileOutputStream(resource.getFile()));
        } catch (IOException e) {
            System.out.println("---保存数据到本地失败");
            e.printStackTrace();
        }
    }

    /**
     * 加载cookie到内存
     */
    public static void loadCookie() {
        InputStream resourceAsStream = MyCookieStore.class.getResourceAsStream(COOKIE_FILE_PATH);
        String cookieTxt = null;
        try {
            cookieTxt = IOUtils.toString(resourceAsStream);
            if (StringUtils.isNotBlank(cookieTxt)) {
                //转成对象
                ConcurrentHashMap<String, ConcurrentHashMap<String, Cookie>> nativeCookie = JsonUtils.jsonToObj(cookieTxt,
                        new TypeReference<ConcurrentHashMap<String, ConcurrentHashMap<String, Cookie>>>() {});
                cookieStore = nativeCookie;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载token到内存
     */
    public static void loadToken() {
        InputStream resourceAsStream = MyCookieStore.class.getResourceAsStream(TOKEN_FILE_PATH);
        String s = null;
        try {
            s = IOUtils.toString(resourceAsStream);
            TOKEN = s;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void log(String log) {
        if (!debug) {
            return;
        }
        System.out.println(log);
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
            log( "IOException in decodeCookie"+e);
        } catch (ClassNotFoundException e) {
            log( "ClassNotFoundException in decodeCookie"+e);
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
