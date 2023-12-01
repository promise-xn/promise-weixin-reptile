package com.example.weixinapireptile.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.weixinapireptile.api.WeiXinApi;
import com.example.weixinapireptile.common.exceptions.BizException;
import com.example.weixinapireptile.mapper.WxArticleMapper;
import com.example.weixinapireptile.model.Article;
import com.example.weixinapireptile.model.BizData;
import com.example.weixinapireptile.model.WxResultBody;
import com.example.weixinapireptile.common.okhttp.MyCookieStore;
import com.example.weixinapireptile.pojo.entity.WxArticle;
import com.example.weixinapireptile.pojo.entity.WxArticleClassify;
import com.example.weixinapireptile.pojo.query.WxArticleQuery;
import com.example.weixinapireptile.pojo.vo.WxArticleVO;
import com.example.weixinapireptile.reptile.SpiderUtil;
import com.example.weixinapireptile.service.IWxArticleClassifyService;
import com.example.weixinapireptile.service.IWxArticleService;
import com.example.weixinapireptile.common.utils.HttpUtils;
import com.example.weixinapireptile.common.utils.ThreadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author xunuo
 * @description: TODO
 * @datetime 2023年 11月 03日 11:28
 */
@Service
public class WxArticleServiceImpl extends ServiceImpl<WxArticleMapper, WxArticle> implements IWxArticleService {

    @Autowired
    private IWxArticleClassifyService articleClassifyService;

    @Autowired
    private WxArticleMapper wxArticleMapper;

    @Override
    public void reptileWxArticle() {
        try {
            // ok
            List<WxArticleClassify> classifyList = articleClassifyService.list();
            if (CollUtil.isNotEmpty(classifyList)){
                for (WxArticleClassify classify : classifyList) {
                    WxResultBody<List<BizData>> searchBiz = WeiXinApi.searchBiz(classify.getClassifyName());

                    List<BizData> list = searchBiz.getList();
                    if (list.size() <= 0) {
                        throw new BizException("公众号:"+classify.getClassifyName()+"不存在");
                    }
                    WxResultBody<List<Article>> findExList = WeiXinApi.findExList(list.get(0).getFakeid());
                    List<Article> exList = findExList.getApp_msg_list();
                    for (Article article : exList) {
                        WxArticle selectOne = baseMapper.selectOne(new LambdaQueryWrapper<WxArticle>()
                                .eq(WxArticle::getTitle, article.getTitle()).eq(WxArticle::getLink, article.getLink()));
                        if (ObjectUtil.isEmpty(selectOne)){

                            LocalDate localDateTime = LocalDateTime.ofEpochSecond(article.getCreate_time(), 0, ZoneOffset.ofHours(8)).toLocalDate();
                            String content = SpiderUtil.getContent(article.getLink());
                            // 持久化入库
                            WxArticle wxArticle = new WxArticle();
                            wxArticle.setId(article.getAid());
                            wxArticle.setTitle(article.getTitle());
                            wxArticle.setLink(article.getLink());
                            wxArticle.setCoverImage(article.getCover());
                            wxArticle.setClassifyId(classify.getId());
                            wxArticle.setPublishDate(localDateTime.toString());
                            wxArticle.setContent(content);
                            baseMapper.insert(wxArticle);
                        }
                    }
                    // 线程阻塞，防止访问频繁导致封号
                    ThreadUtil.wait(3);
                }
            }
        } catch (BizException e) {
            log.error("公众号数据获取异常：{}", e);
        } catch (Exception e) {
            log.error("微信请求异常：{}", e);
        }
    }

    @Override
    public List<WxArticleVO> listPages(WxArticleQuery wxArticleQuery) {
        return wxArticleMapper.listWxArticleVo(wxArticleQuery);
    }

    @Override
    public WxArticleVO getWxArticleById(String id) {
        return wxArticleMapper.getWxArticleById(id);
    }
}
