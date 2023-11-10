package com.example.weixinapireptile.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * wx文章分类 VO
 * @author xunuo
 * @date 10:39 2023/11/3
 **/
@Data
@ApiModel(value = "微信文章分类-视图")
public class WxArticleClassifyVO {

    @ApiModelProperty(value = "公众号分类id")
    private String id;

    @ApiModelProperty(value = "分类名称")
    private String classifyName;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}
