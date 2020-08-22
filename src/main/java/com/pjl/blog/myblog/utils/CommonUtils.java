package com.pjl.blog.myblog.utils;

import com.pjl.blog.myblog.dto.PaginationDto;

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
     * @desc 构建分页实体（总页数、页数和偏移量）
     * @param totalCount
     * @param page
     * @param size
     * @return
     */
    public static <T> PaginationDto buildPage(T t, Integer totalCount, Integer page, Integer size){
        Integer totalPage = CommonUtils.calculateTotalPage(totalCount);
        Integer offset = CommonUtils.calculatePageOffset(totalPage, page,size);
        PaginationDto<T> pagination = new PaginationDto<T>();
        pagination.setOffset(offset);
        pagination.setPagination(totalPage,page);
        return pagination;
    }


    /**
     * @desc 反射转换对象为map(只转换属性值不为空的)
     * @param obj
     * @return
     */
    public static Map<String,Object> objectValueToMap(Object obj) throws IllegalAccessException {
        Map<String,Object> map = new HashMap<>();
        Class<?> clazz = obj.getClass();
        for (Field declaredField : clazz.getDeclaredFields()) {
            declaredField.setAccessible(true);
            Object value = declaredField.get(obj);
            if (value != null){
                map.put(declaredField.getName(),value);
            }
        }
        Class<?> supClazz = clazz.getSuperclass();
        for (Field declaredField : clazz.getDeclaredFields()) {
            declaredField.setAccessible(true);
            Object value = declaredField.get(obj);
            if (value != null){
                map.put(declaredField.getName(),value);
            }
        }
        return map;
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
        for (Field declaredField : clazz.getDeclaredFields()) {
            declaredField.setAccessible(true);
            Object value = declaredField.get(obj);
            if (value != null){
                fieldNameList.add(declaredField.getName());
            }
        }
        Class<?> supClazz = clazz.getSuperclass();
        if (supClazz != null){
            for (Field declaredField : clazz.getDeclaredFields()) {
                declaredField.setAccessible(true);
                Object value = declaredField.get(obj);
                if (value != null){
                    fieldNameList.add(declaredField.getName());
                }
            }
        }
        return fieldNameList;
    }

}
