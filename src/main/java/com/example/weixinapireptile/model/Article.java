package com.example.weixinapireptile.model;

import lombok.Data;

/**
 * 文章数据
 * @author zsq
 * @date 2021/3/31 - 21:54
 */
@Data
public class Article {

    /**
     * 标题
     **/
    private String title;
    /**
     * 文章连接
     **/
    private String link;
    /**
     * 发布时间
     **/
    private Long create_time;

    /**
     * 封面图
     **/
    private String cover;




}
