package com.example.weixinapireptile.controller;

import com.example.weixinapireptile.api.RedisCache;
import com.example.weixinapireptile.api.WeiXinApi;
import com.example.weixinapireptile.common.base.BaseController;
import com.example.weixinapireptile.common.result.PageResult;
import com.example.weixinapireptile.common.result.Result;
import com.example.weixinapireptile.model.WxResultBody;
import com.example.weixinapireptile.common.okhttp.MyCookieStore;
import com.example.weixinapireptile.pojo.query.WxArticleQuery;
import com.example.weixinapireptile.pojo.vo.WxArticleVO;
import com.example.weixinapireptile.service.IWxArticleService;
import com.example.weixinapireptile.common.utils.HttpUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * 微信公众号爬取-API
 * @author xunuo
 * @datetime 2023年 10月 26日 15:01
 */
@Api(tags = "微信文章爬取API")
@RestController
@RequestMapping("/reptile/wxArticle")
@Slf4j
public class WxArticleController extends BaseController {


    @Autowired
    private IWxArticleService wxArticleService;
    @Autowired
    RedisCache redisCache;

    /**
     * 生成二维码
     * @author xunuo
     * @date 15:42 2023/11/3
     * @param resp
     **/
    @ApiOperation(value = "生成二维码")
    @GetMapping ("/getImage")
    public void getImage(HttpServletResponse resp) throws IOException {
        // 1. POST请求开始登录接口，初始化cookie
        redisCache.deleteObject("cookie");
        redisCache.deleteObject("token");
        resp.setContentType("image/jpeg");
        // 1. POST请求开始登录接口，初始化cookie
        String sessionid = "" + System.currentTimeMillis() + (int) (Math.random() * 100);
        WxResultBody wxResultBody = WeiXinApi.startLogin(sessionid);
        // 2. 请求获取二维码图片接口，得到流
        InputStream inputStream = WeiXinApi.getQRCode();
        // 从输入流中读取图片数据
        BufferedImage image = ImageIO.read(inputStream);
        //转成图片显示
        ServletOutputStream outputStream = resp.getOutputStream();
        ImageIO.write(image, "jpg", outputStream);
        outputStream.close();
        outputStream.flush();

    }

    /**
     * 查询登录状态
     * @author xunuo
     * @date 10:41 2023/11/8
     **/
    @ApiOperation(value = "查询登录状态")
    @RequestMapping("/askQRCode")
    public Result askQRCode() throws IOException{
        WxResultBody askQrCode = WeiXinApi.askQRCode();
        Integer status = askQrCode.getStatus();
        if (status != 1) {
            return Result.failed("用户未登录！");
        }
        WxResultBody bizlogin = WeiXinApi.bizlogin();
        if (StringUtils.isNotEmpty(bizlogin.getErr_msg())){
            return Result.failed("登陆失败！");
        }
        //重定向地址
        String redirect_url = bizlogin.getRedirect_url();
        //解析成键值对
        Map<String, String> loginRes = HttpUtils.parseQueryParams(redirect_url);
        //得到token
        String token = loginRes.get("token");
        //设置全局token值
        MyCookieStore.setToken(token);
        // TODO 把token持久化
        redisCache.setCacheObject("token",token);
        return Result.success();
    }

    /**
     * 处理数据爬取
     * @author xunuo
     * @date 15:42 2023/11/3
     * @return org.extractor.common.result.Result
     **/
    @ApiOperation(value = "开始爬取数据")
    @PostMapping("/start")
    public Result bizLogin() {
        String token = redisCache.getCacheObject("token");
        if (token==null){
            return Result.failed("请先扫码登录！");
        }
        MyCookieStore.setToken(token);
        wxArticleService.reptileWxArticle();
        return Result.success();
    }

    @GetMapping("/pages")
    @ApiOperation(value = "分页列表")
    public PageResult<WxArticleVO> list(WxArticleQuery wxArticleQuery) {
        startPage();
        return PageResult.success(wxArticleService.listPages(wxArticleQuery));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "详情")
    public Result<WxArticleVO> list(@PathVariable String id) {
        return Result.success(wxArticleService.getWxArticleById(id));
    }

}
