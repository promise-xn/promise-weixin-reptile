package com.example.weixinapireptile.model;

/**
 * 公众号数据
 * @author zsq
 * @date 2021/3/31 - 21:38
 */
public class BizData {

    private String fakeid;

    private String nickname;

//    ...还有很多参数


    public String getFakeid() {
        return fakeid;
    }

    public void setFakeid(String fakeid) {
        this.fakeid = fakeid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "BizData{" +
                "fakeid='" + fakeid + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
