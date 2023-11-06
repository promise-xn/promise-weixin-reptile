package com.example.weixinapireptile.model;

/**
 * 文章数据
 * @author zsq
 * @date 2021/3/31 - 21:54
 */
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



    //...还有很多参数

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Article{" +
                "title='" + title + '\'' +
                "link='" + link + '\'' +
                '}';
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
