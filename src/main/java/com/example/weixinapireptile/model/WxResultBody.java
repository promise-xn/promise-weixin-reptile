package com.example.weixinapireptile.model;

/**
 * 微信接口返回内容
 * @author zsq
 * @version 1.0
 * @date 2021/3/31 11:17
 */
public class WxResultBody<L> {

    private Integer acct_size;

    private String err_msg;

    private String redirect_url;

    private Integer ret;

    private Integer status;

    private Integer user_category;

    private BaseResp base_resp;

    private L list;

    private L app_msg_list;

    public BaseResp getBase_resp() {
        return base_resp;
    }

    public void setBase_resp(BaseResp base_resp) {
        this.base_resp = base_resp;
    }

    public Integer getAcct_size() {
        return acct_size;
    }

    public void setAcct_size(Integer acct_size) {
        this.acct_size = acct_size;
    }

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getUser_category() {
        return user_category;
    }

    public void setUser_category(Integer user_category) {
        this.user_category = user_category;
    }

    public String getRedirect_url() {
        return redirect_url;
    }

    public void setRedirect_url(String redirect_url) {
        this.redirect_url = redirect_url;
    }

    public L getList() {
        return list;
    }

    public void setList(L list) {
        this.list = list;
    }

    public L getApp_msg_list() {
        return app_msg_list;
    }

    public void setApp_msg_list(L app_msg_list) {
        this.app_msg_list = app_msg_list;
    }

    @Override
    public String toString() {
        return "WxResultBody{" +
                "acct_size=" + acct_size +
                ", err_msg='" + err_msg + '\'' +
                ", redirect_url='" + redirect_url + '\'' +
                ", ret=" + ret +
                ", status=" + status +
                ", user_category=" + user_category +
                ", base_resp=" + base_resp +
                ", list=" + list +
                ", app_msg_list=" + app_msg_list +
                '}';
    }
}
