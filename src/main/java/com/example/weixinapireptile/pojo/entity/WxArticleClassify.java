package com.example.weixinapireptile.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.weixinapireptile.common.base.BaseEntity;
import lombok.Data;

/**
 * 系统文章分类 实体对象
 *
 * @author linyan-z7
 * @date 2023/10/26
 */
@Data
@TableName(value = "wx_article_classify")
public class WxArticleClassify extends BaseEntity {

    /**
     * 分类名称
     */
    @TableField("classify_name")
    private String classifyName;


}
