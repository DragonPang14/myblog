package com.pjl.blog.myblog.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonUtils {

    /**
     * @desc 计算分页偏移量
     * @param totalPage
     * @param page
     * @param size
     * @return
     */
    public static Integer calculatePageOffset(Integer totalPage,Integer page,Integer size){
        if (page < 1) {
            page = 1;
        } else if (page > totalPage) {
            return null;
        }
//        偏移量
        Integer offset = size * (page - 1);
        return offset;
    }

    /**
     * @desc 计算分页总页数
     * @param totalCount
     * @return
     */
    public static Integer calculateTotalPage(Integer totalCount){
        Integer totalPage ;
        if (totalCount != 0){
            totalPage = totalCount % 10 == 0 ? totalCount / 10 : (totalCount / 10) + 1;
        }else {
            totalPage = 1;
        }
        return totalPage;
    }

    /**
     * @desc 反射转换对象为map(只转换属性值不为空的)
     * @param obj
     * @return
     */
    public static Map<String,Object> objectValueToMap(Object obj) throws IllegalAccessException {
        Map<String,Object> map = new HashMap<>();
        Class<?> clazz = obj.getClass();
        notNullValueMap(obj, map, clazz);
        Class<?> supClazz = clazz.getSuperclass();
        notNullValueMap(obj, map, supClazz);
        return map;
    }

    /**
     * @desc 转换对象为map
     * @param obj
     * @param map
     * @param clazz
     * @throws IllegalAccessException
     */
    private static void notNullValueMap(Object obj, Map<String, Object> map, Class<?> clazz) throws IllegalAccessException {
        for (Field declaredField : clazz.getDeclaredFields()) {
            declaredField.setAccessible(true);
            Object value = declaredField.get(obj);
            if (value != null){
                map.put(declaredField.getName(),value);
            }
        }
    }

    /**
     * @desc 提取对象的属性名称（只提取属性值不为空的）
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    public static List<String> getObjectUsedFieldName(Object obj) throws IllegalAccessException {
        List<String> fieldNameList = new ArrayList<>();
        Class<?> clazz = obj.getClass();
        //把非空字段属性名提取
        notNullFieldList(obj, fieldNameList, clazz);
        Class<?> supClazz = clazz.getSuperclass();
        if (supClazz != null){
            notNullFieldList(obj, fieldNameList, supClazz);
        }
        return fieldNameList;
    }

    /**
     * @desc 提取对象属性名称
     * @param obj
     * @param fieldNameList
     * @param clazz
     * @throws IllegalAccessException
     */
    private static void notNullFieldList(Object obj, List<String> fieldNameList, Class<?> clazz) throws IllegalAccessException {
        for (Field declaredField : clazz.getDeclaredFields()) {
            declaredField.setAccessible(true);
            Object value = declaredField.get(obj);
            if (value != null){
                fieldNameList.add(declaredField.getName());
            }
        }
    }
}
