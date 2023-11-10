package com.example.weixinapireptile.common.result;

import com.alibaba.fastjson2.JSON;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HttpServletResult {

    public static void result(HttpServletResponse response, String status, String message) {
        result(response, status, message, null);
    }

    public static void result(HttpServletResponse response, ResultStatus resultStatus) {
        result(response, resultStatus.getStatus(), resultStatus.getMessage(), null);
    }

    public static <T> void result(HttpServletResponse response, String status, String message, T data) {
        try {
            response.setStatus(HttpStatus.OK.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            Result<T> result = new Result<>();
            result.setStatus(status);
            result.setMessage(message);
            result.setData(data);
            response.getWriter().print(JSON.toJSONString(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
