package com.example.weixinapireptile.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.weixinapireptile.mapper.WxArticleClassifyMapper;
import com.example.weixinapireptile.pojo.entity.WxArticleClassify;
import com.example.weixinapireptile.pojo.query.WxArticleClassifyQuery;
import com.example.weixinapireptile.pojo.vo.WxArticleClassifyVO;
import com.example.weixinapireptile.service.IWxArticleClassifyService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xunuo
 * @description: TODO
 * @datetime 2023年 11月 03日 11:28
 */
@Service
public class WxArticleClassifyServiceImpl extends ServiceImpl<WxArticleClassifyMapper, WxArticleClassify> implements IWxArticleClassifyService {

    @Override
    public List<WxArticleClassifyVO> WxArticleClassifyList(WxArticleClassifyQuery wxArticleClassifyQuery) {
        return null;
    }
}
