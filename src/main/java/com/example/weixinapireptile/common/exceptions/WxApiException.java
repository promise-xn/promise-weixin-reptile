package com.example.weixinapireptile.common.exceptions;

import com.example.weixinapireptile.common.enums.WxResultStatus;

/**
 * @author zsq
 * @version 1.0
 * @date 2021/3/31 14:37
 */
public class WxApiException extends RuntimeException {

    /**错误枚举**/
    private WxResultStatus wxResultStatus;

    public WxApiException(WxResultStatus wxResultStatus) {
        super(wxResultStatus.name());
        this.wxResultStatus = wxResultStatus;
    }

    public WxApiException(WxResultStatus wxResultStatus, Exception e) {
        super(wxResultStatus.name(), e);
        this.wxResultStatus = wxResultStatus;
    }

    public WxResultStatus getWxResultStatus() {
        return wxResultStatus;
    }

}
