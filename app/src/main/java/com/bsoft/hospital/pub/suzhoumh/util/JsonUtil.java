package com.bsoft.hospital.pub.suzhoumh.util;

import com.alibaba.fastjson.JSON;
import com.daoyixun.location.ipsmap.utils.T;

import java.util.List;

/**
 * @author :lizhengcao
 * @date :2019/1/23
 * E-mail:lizc@bsoft.com.cn
 * @类说明 fastJson工具类 json转化为对象,json转化为数组
 */
public class JsonUtil {

    /**
     * json转化为对象
     */
    public static <T> T jsonToObject(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }

    /**
     * json转化为数组
     */
    public static <T> List<T> jsonToArray(String json, Class<T> clazz) {
        return JSON.parseArray(json, clazz);
    }

    /**
     * 对象转化为json
     */
    public static String objectToString(Object object) {
        return JSON.toJSONString(object);
    }

}
