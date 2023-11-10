package com.example.weixinapireptile.common.enums;

/**
 * @author zsq
 * @version 1.0
 * @date 2021/3/31 14:19
 */
public enum WxResultStatus {

    /**请求成功**/
    SUCCESS(200, "成功"),
    /**请求失败**/
    FAIL(500, "请求失败"),
    /**返回结果对象解析失败**/
    FAIL_NULL_RES(510, "返回结果对象解析失败"),
    /**返回结果为失败状态**/
    FAIL_STATUS(520, "返回结果为失败状态"),
    /**读取io流失败**/
    FAIL_IO_ERR(530, "读取io流失败")
    ;

    private int status;
    private String name;

    WxResultStatus(int status, String name){
        this.status = status;
        this.name = name;
    }

}
