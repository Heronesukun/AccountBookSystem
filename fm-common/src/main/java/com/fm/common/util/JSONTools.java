package com.fm.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * @Author hitsune
 * @Company kafu chino
 * @Description
 * @Time 2024/12/5 16:30
 */

public class JSONTools {
    private static ObjectMapper mapper = new ObjectMapper();

    /**
     * JSON转换为集合
     *
     * @param json
     * @param clazz
     * @return
     * @param <T>
     */
    public static <T> List<T> json2list(String json, Class<T> clazz){
        try {
            return mapper.readValue(json, new TypeReference<List<T>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * JSON转换为对象
     * @param json
     * @param clazz
     * @return
     * @param <T>
     */
    public static <T> T json2object(String json,Class<T> clazz){
        try {
            return mapper.readValue(json,clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 对象转换为JSON
     * @param obj
     * @return
     */
    public static String object2json(Object obj){
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
