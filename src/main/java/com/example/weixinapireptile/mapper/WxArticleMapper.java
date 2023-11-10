package com.example.weixinapireptile.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.weixinapireptile.pojo.entity.WxArticle;
import com.example.weixinapireptile.pojo.query.WxArticleQuery;
import com.example.weixinapireptile.pojo.vo.WxArticleVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * wx文章 mapper
 * @author xunuo
 * @date 11:33 2023/11/3
 **/
@Mapper
public interface WxArticleMapper extends BaseMapper<WxArticle> {

    List<WxArticleVO> listWxArticleVo(WxArticleQuery wxArticleQuery);

    WxArticleVO getWxArticleById(String id);
}
