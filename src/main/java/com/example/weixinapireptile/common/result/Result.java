package com.example.weixinapireptile.common.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "请求响应")
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "响应状态")
    private String status;

    @ApiModelProperty(value = "响应消息")
    private String message;

    @ApiModelProperty(value = "响应数据")
    private T data;

    /**
     * 请求成功 ---------------------------------------------------------------------
     */
    public static <T> Result<T> success() {
        return result(null, ResultStatus.SUCCESS);
    }

    public static <T> Result<T> success(T data) {
        return result(data, ResultStatus.SUCCESS);
    }

    public static <T> Result<T> success(T data, String message) {
        return result(data, ResultStatus.SUCCESS.getStatus(), message);
    }

    /**
     * 请求失败 ---------------------------------------------------------------------
     */
    public static <T> Result<T> failed() {
        return result(null, ResultStatus.FAILURE);
    }

    public static <T> Result<T> failed(T data) {
        return result(data, ResultStatus.FAILURE);
    }

    public static <T> Result<T> failed(ResultStatus resultStatus) {
        return result(null, resultStatus);
    }

    public static <T> Result<T> failed(String message) {
        return result(null, ResultStatus.FAILURE.getStatus(), message);
    }

    public static <T> Result<T> failed(T data, String message) {
        return result(data, ResultStatus.FAILURE.getStatus(), message);
    }

    public static <T> Result<T> failed(T data, ResultStatus resultStatus) {
        return result(data, resultStatus);
    }

    /**
     * 判断结果 ---------------------------------------------------------------------
     */
    public static <T> Result<T> judge(int data) {
        if (data > 0) {
            return success();
        }
        return failed();
    }

    public static <T> Result<T> judge(boolean data) {
        if (data) {
            return success();
        }
        return failed();
    }

    public static <T> Result<List<T>> judge(List<T> list) {
        if (list == null || list.isEmpty()) {
            return failed();
        }
        return success();
    }

    /**
     * 自定义 ---------------------------------------------------------------------
     */
    public static <T> Result<T> result(T data, ResultStatus resultStatus) {
        return new Result<T>() {{
            setStatus(resultStatus.getStatus());
            setMessage(resultStatus.getMessage());
            setData(data);
        }};
    }

    public static <T> Result<T> result(ResultStatus resultStatus) {
        return new Result<T>() {{
            setStatus(resultStatus.getStatus());
            setMessage(resultStatus.getMessage());
        }};
    }

    /*** {@docRoot 公共响应方法(私有)} */
    private static <T> Result<T> result(T data, String status, String message) {
        return new Result<T>() {{
            setStatus(status);
            setMessage(message);
            setData(data);
        }};
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
