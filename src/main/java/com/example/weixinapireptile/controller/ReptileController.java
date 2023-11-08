package com.example.weixinapireptile.controller;

import com.alibaba.fastjson2.JSONObject;
import com.example.weixinapireptile.api.RedisCache;
import com.example.weixinapireptile.api.WeiXinApi;
import com.example.weixinapireptile.awt.MyImageShowFrame;
import com.example.weixinapireptile.model.Article;
import com.example.weixinapireptile.model.BizData;
import com.example.weixinapireptile.model.WxResultBody;
import com.example.weixinapireptile.okhttp.MyCookieStore;
import com.example.weixinapireptile.reptile.Resp;
import com.example.weixinapireptile.reptile.SpiderUtil;
import com.example.weixinapireptile.utils.HttpUtils;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author xunuo
 * @description: TODO
 * @datetime 2023年 10月 26日 15:01
 */

@RestController
@RequestMapping("/reptile")
public class ReptileController {

    @Autowired
    RedisCache redisCache;

    /**
     * 生成二维码
     * @author xunuo
     * @date 16:26 2023/10/26
     * @return org.springframework.http.ResponseEntity<byte[]>
     **/
    @RequestMapping("/getImage")
    public void getImage(HttpServletResponse resp) throws IOException {
        // 1. POST请求开始登录接口，初始化cookie
        redisCache.deleteObject("cookie");
        redisCache.deleteObject("token");
        String sessionid = "" + System.currentTimeMillis() + (int)(Math.random()*100);
        WxResultBody wxResultBody = WeiXinApi.startLogin(sessionid);
        System.out.println("---请求开始登录接口 返回结果:" + wxResultBody.toString());
        // 2. 请求获取二维码图片接口，得到流
        InputStream inputStream = WeiXinApi.getQRCode();
        // 从输入流中读取图片数据
        BufferedImage image = ImageIO.read(inputStream);
        ServletOutputStream stream = resp.getOutputStream();
        ImageIO.write(image, "jpg", stream);
    }

    @RequestMapping("/askQRCode")
    public void askQRCode(String cookie) throws IOException{
        WxResultBody askQrCode = WeiXinApi.askQRCode();
        Integer status = askQrCode.getStatus();
        if (status != 1) {
            return;
        }
        WxResultBody bizlogin = WeiXinApi.bizlogin();
        //重定向地址
        String redirect_url = bizlogin.getRedirect_url();
        //解析成键值对
        Map<String, String> loginRes = HttpUtils.parseQueryParams(redirect_url);
        //得到token
        String token = loginRes.get("token");
        //设置全局token值
        MyCookieStore.setToken(token);
        redisCache.setCacheObject("token",token);
        System.out.println("---恭喜你，登录成功！");
    }

    @RequestMapping("/article")
    public void article() throws IOException {
        while (true) {
            String gzName = "劳动午报";
            // TODO 公众号写死遍历搜索，默认都选 1
            // TODO 遍历搜索时，线程sleep5-10s后再进行下一条
            WxResultBody<List<BizData>> searchBiz = WeiXinApi.searchBiz(gzName);
            System.out.println("----请按序号选择公众号");
            List<BizData> list = searchBiz.getList();
            if (list.size()<=0){
                return;
            }
            BizData select = list.get(0);
            System.out.println(String.format("--好的，开始搜索【%s】的文章...", select.getNickname()));

            WxResultBody<List<Article>> findExList = WeiXinApi.findExList(select.getFakeid());
            List<Article> exList = findExList.getApp_msg_list();
            exList.forEach(a->{
                System.out.println("转换类型接受的文章数据----------------------"+exList);
            });
            for (Article article : exList) {
                System.out.println("---" + article.getTitle());
                System.out.println("****" + article.getLink());
                Resp<JSONObject> resp = SpiderUtil.getActicle(article.getLink());
                if (resp.isSuccess()) {
                    // TODO 持久化入库
                    System.out.println(resp.getBody());
                } else {
                    System.out.println(resp.getMsg());
                }

            }
            return;
        }


    }

    @RequestMapping("/getReids")
    public Resp<?> getRedis(){
//        Boolean cookie = redisTemplate.hasKey("cookie");
//        if (cookie){
//            Object cookie1 = redisTemplate.opsForValue().get("cookie");
//            System.out.println(cookie1);
//            return Resp.success(cookie1);
//        }
        redisCache.setCacheObject("cookie","1234");
        redisCache.setCacheObject("cookie","098765");
        boolean hasKey = redisCache.hasKey("cookie");
        if (hasKey){
            Object cookie = redisCache.getCacheObject("cookie");
            return Resp.success(cookie);
        }
        return Resp.error();
    }

}
