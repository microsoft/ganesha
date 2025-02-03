package com.microsoft.ganesha.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class JSONUtil {

    /**
     * Method converts class object to json string
     *
     * @param <T>
     * @param object
     * @return String
     */
    public static <T> String convertObjectToJson(T object) {
        ObjectMapper objMapper = new ObjectMapper();
        objMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String json = null;
        try {
            json = objMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * Method converts JSON String to Object
     *
     * @param <T>
     * @param json
     * @param objType
     * @return T
     */
    public static <T> T convertJsonToObject(String json, Class<T> objType) {
        ObjectMapper objMapper = new ObjectMapper();
        objMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        T obj = null;
        try {
            obj = objMapper.readValue(json, objType);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * Method converts class object to Map of String key and Object value
     *
     * @param <T,K>
     * @param object
     * @param objType
     * @return String
     */
    public static <T, K> Map<String, K> convertObjectToMap(T object, Class<K> objType) {
        ObjectMapper objMapper = new ObjectMapper();
        Map<String, K> map = new HashMap<>();
        map = objMapper.convertValue(object, new TypeReference<Map<String, K>>() {
        });
        return map;
    }
}
