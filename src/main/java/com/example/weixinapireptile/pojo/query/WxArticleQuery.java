package com.example.weixinapireptile.pojo.query;

import com.example.weixinapireptile.common.base.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * wx文章 实体
 * @author xunuo
 * @date 10:39 2023/11/3
 **/
@Data
@ApiModel(value = "微信文章-查询")
public class WxArticleQuery extends BaseQuery {

    /**
     * 分类ID
     */
    @ApiModelProperty(value = "分类ID")
    private String classifyId;
    /**
     * 搜索条件
     */
    @ApiModelProperty(value = "搜索条件")
    private String keyword;



}
