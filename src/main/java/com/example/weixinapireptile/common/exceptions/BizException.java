package com.example.weixinapireptile.common.exceptions;

import com.example.weixinapireptile.common.result.ResultStatus;
import lombok.Getter;

/**
 * 业务异常
 */
@Getter
public class BizException extends RuntimeException {
    private static final long serialVersionUID = -7285211528095468156L;

    private ResultStatus resultStatus;

    private BizException() {
    }

    public BizException(String message) {
        super(message);
    }

    public BizException(ResultStatus resultStatus) {
        super(resultStatus.getMessage());
        this.resultStatus = resultStatus;
    }

    public BizException(Throwable cause) {
        super(cause);
    }

    public BizException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

    public BizException(String errorMessage, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(errorMessage, cause, enableSuppression, writableStackTrace);
    }

    public static void throwe(String errorMessage) {
        throw new BizException(errorMessage);
    }

    public static void throwe(ResultStatus resultStatus) {
        throw new BizException(resultStatus);
    }
}
