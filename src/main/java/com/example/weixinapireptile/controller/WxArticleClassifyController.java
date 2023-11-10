package com.example.weixinapireptile.controller;

import com.example.weixinapireptile.common.base.BaseController;
import com.example.weixinapireptile.common.result.Result;
import com.example.weixinapireptile.pojo.query.WxArticleClassifyQuery;
import com.example.weixinapireptile.pojo.vo.WxArticleClassifyVO;
import com.example.weixinapireptile.service.IWxArticleClassifyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 微信文章分类-API
 * @author xunuo
 * @datetime 2023年 10月 26日 15:01
 */
@Api(tags = "微信文章分类-API")
@RestController
@RequestMapping("/reptile/wxArticleClassify")
@Slf4j
public class WxArticleClassifyController extends BaseController {

    @Autowired
    private IWxArticleClassifyService wxArticleClassifyService;

    @GetMapping("/pages")
    @ApiOperation(value = "分页列表")
    public Result<List<WxArticleClassifyVO>> list(WxArticleClassifyQuery wxArticleClassifyQuery) {
        return Result.success(wxArticleClassifyService.WxArticleClassifyList(wxArticleClassifyQuery));
    }


}
