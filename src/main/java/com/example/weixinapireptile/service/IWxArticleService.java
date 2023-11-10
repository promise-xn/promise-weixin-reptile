package com.example.weixinapireptile.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.weixinapireptile.pojo.entity.WxArticle;
import com.example.weixinapireptile.pojo.query.WxArticleQuery;
import com.example.weixinapireptile.pojo.vo.WxArticleVO;

import java.util.List;

/**
 * @author xunuo
 * @description: TODO
 * @datetime 2023年 11月 03日 10:57
 */
public interface IWxArticleService extends IService<WxArticle> {

     void reptileWxArticle();

     List<WxArticleVO> listPages(WxArticleQuery wxArticleQuery);

     WxArticleVO getWxArticleById(String id);
}
