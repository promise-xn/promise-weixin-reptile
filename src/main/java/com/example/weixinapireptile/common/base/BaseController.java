package com.example.weixinapireptile.common.base;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 基础 Controller
 *
 * @author zhanglincheng
 * @date 2022/10/1
 */
public class BaseController {
    protected final Logger log = LoggerFactory.getLogger(BaseController.class);

    private static final String PAGE_NUM = "pageNum";
    private static final String PAGE_SIZE = "pageSize";
    private static final Integer DEFAULT_PAGE_NUM = 1;
    private static final Integer DEFAULT_PAGE_SIZE = 10;

    /**
     * 数据分页
     */
    protected void startPage() {
        List<Integer> params = buildPageParam();
        if (!params.isEmpty()) {
            PageHelper.startPage(params.get(0), params.get(1));
        }
    }

    protected static List<String> toList(String values) {
        if (values != null && values.length() > 0) {
            return Arrays.asList(values.split(","));
        }
        return new ArrayList<>();
    }

    private List<Integer> buildPageParam() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        List<Integer> r = new ArrayList<>(2);
        String pageNum = null;
        String pageSize = null;
        // json
        JSONObject json = this.getJson(request);
        if (json != null) {
            pageNum = json.getString(PAGE_NUM);
            pageSize = json.getString(PAGE_SIZE);
        } else {
            // form-data、x-www-form-urlencoded
            Map<String, String[]> parameterMap = request.getParameterMap();
            String[] pn = parameterMap.get(PAGE_NUM);
            String[] ps = parameterMap.get(PAGE_SIZE);
            if (pn != null && ps != null) {
                pageNum = pn[0];
                pageSize = ps[0];
            }
        }
        if (pageNum == null || pageSize == null) {
            r.add(DEFAULT_PAGE_NUM);
            r.add(DEFAULT_PAGE_SIZE);
        } else {
            r.add(Integer.parseInt(pageNum));
            r.add(Integer.parseInt(pageSize));
        }
        return r;
    }

    private JSONObject getJson(HttpServletRequest request) {
        if (request.getContentType() != null && request.getContentType().contains(MediaType.APPLICATION_JSON_VALUE)) {
            String bodyStr = getBodyStr(request);
            if (bodyStr.isEmpty()) {
                return null;
            }
            return JSON.parseObject(bodyStr);
        } else {
            return null;
        }
    }

    private String getBodyStr(HttpServletRequest request) {
        StringBuffer sb = new StringBuffer();
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            inputStream = request.getInputStream();
            reader = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            char[] bodyCharBuffer = new char[1024];
            int len = 0;
            while ((len = reader.read(bodyCharBuffer)) != -1) {
                sb.append(new String(bodyCharBuffer, 0, len));
            }
        } catch (Exception e) {
            log.error("getBodyStr():{}", e.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
}
