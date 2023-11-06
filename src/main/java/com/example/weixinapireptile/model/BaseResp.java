package com.example.weixinapireptile.model;

/**
 * @author zsq
 * @version 1.0
 * @date 2021/3/31 11:18
 */
public class BaseResp {

    private String err_msg;

    private Integer ret;

    public String getErr_msg() {
        return err_msg;
    }

    public void setErr_msg(String err_msg) {
        this.err_msg = err_msg;
    }

    public Integer getRet() {
        return ret;
    }

    public void setRet(Integer ret) {
        this.ret = ret;
    }

    @Override
    public String toString() {
        return "BaseResp{" +
                "err_msg='" + err_msg + '\'' +
                ", ret=" + ret +
                '}';
    }
}
