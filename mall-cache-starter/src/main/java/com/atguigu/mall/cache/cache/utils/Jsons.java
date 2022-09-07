package com.atguigu.mall.cache.cache.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;


/**
 * Author：张世平
 * Date：2022/8/31 13:54
 */
public class Jsons {
    private static ObjectMapper mapper = new ObjectMapper();
    /**
     * 把对象转为json字符串
     * @param object
     * @return
     */
    public static String toStr(Object object) {
        //jackson
        try {
            String s = mapper.writeValueAsString(object);
            return s;
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public static<T> T toObject(String obtString, Class<T> cls) {
        if (StringUtils.isEmpty(obtString)){
            return null;
        }
        T t = null;
        try {
            t = mapper.readValue(obtString, cls);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return t;
    }

    public static<T> T toObject(String obtString, TypeReference<T> type) {
        if (StringUtils.isEmpty(obtString)){
            return null;
        }
        T t = null;
        try {
            t = mapper.readValue(obtString, type);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return t;
    }
}
