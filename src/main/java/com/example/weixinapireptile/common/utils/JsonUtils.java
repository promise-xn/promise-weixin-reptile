package com.example.weixinapireptile.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zsq
 * @version 1.0
 * @date 2021/3/31 14:05
 */
public class JsonUtils {

    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
    }

    /**
     * 对象转json字符串
     * @param object
     * @return
     */
    public static String toJsonStr(Object object){
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("json转换出错", e);
        }
    }

    /**
     * json字符转map
     * @param jsonStr
     * @return
     */
    public static Map<String, Object> jsonToMap(String jsonStr) {
        try {
            return objectMapper.readValue(jsonStr, HashMap.class);
        } catch (IOException e) {
            throw new RuntimeException("json转换出错", e);
        }
    }

    /**
     * json转对象
     * @param jsonStr
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T jsonToObj(String jsonStr, Class<T> clazz) {
        try {
            return objectMapper.readValue(jsonStr, clazz);
        } catch (IOException e) {
            throw new RuntimeException("json转换出错", e);
        }
    }

    /**
     * json转对象
     * @param jsonStr
     * @param typeReference
     * @param <T>
     * @return
     */
    public static <T> T jsonToObj(String jsonStr, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(jsonStr, typeReference);
        } catch (IOException e) {
            throw new RuntimeException("json转换出错", e);
        }
    }


}
