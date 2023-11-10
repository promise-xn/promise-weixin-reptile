package com.example.weixinapireptile.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.weixinapireptile.pojo.entity.WxArticleClassify;
import com.example.weixinapireptile.pojo.query.WxArticleClassifyQuery;
import com.example.weixinapireptile.pojo.vo.WxArticleClassifyVO;

import java.util.List;

/**
 * @author xunuo
 * @description: TODO
 * @datetime 2023年 11月 03日 10:57
 */
public interface IWxArticleClassifyService extends IService<WxArticleClassify> {

    List<WxArticleClassifyVO> WxArticleClassifyList(WxArticleClassifyQuery wxArticleClassifyQuery);

}
