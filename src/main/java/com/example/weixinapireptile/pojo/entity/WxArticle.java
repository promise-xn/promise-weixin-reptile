package com.example.weixinapireptile.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.weixinapireptile.common.base.BaseEntity;
import lombok.Data;

/**
 * wx文章 实体
 * @author xunuo
 * @date 10:39 2023/11/3
 **/
@Data
@TableName(value = "wx_article")
public class WxArticle extends BaseEntity {

    /**
     * 分类ID
     */
    @TableField(value = "classify_id")
    private String classifyId;

    /**
     * 正文标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 链接
     */
    @TableField(value = "link")
    private String link;

    /**
     * 封面图
     */
    @TableField(value = "cover_image")
    private String coverImage;

    /**
     * 文章内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 发布日期
     */
    @TableField(value = "publish_date")
    private String publishDate;


}
