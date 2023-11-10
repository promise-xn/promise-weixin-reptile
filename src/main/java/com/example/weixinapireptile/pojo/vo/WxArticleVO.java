package com.example.weixinapireptile.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * wx文章 VO
 * @author xunuo
 * @date 10:39 2023/11/3
 **/
@Data
@ApiModel(value = "微信文章-视图")
public class WxArticleVO {

    @ApiModelProperty(value = "文章id")
    private String id;
    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "文章链接")
    private String link;

    @ApiModelProperty(value = "封面图")
    private String coverImage;

    @ApiModelProperty(value = "发布日期")
    private String publishDate;

    @ApiModelProperty(value = "分类id")
    private String classifyId;

    @ApiModelProperty(value = "分类名称")
    private String classifyName;

    @ApiModelProperty(value = "文章内容")
    private String content;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}
