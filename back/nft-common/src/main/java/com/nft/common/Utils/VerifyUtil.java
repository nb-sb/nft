package com.nft.common.Utils;


import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 校验工具类
 *
 * @author：user
 * @date: 2022-04-13 16:53
 */
public class VerifyUtil {
    /**
     * 校验多个都不为空，只要有一个参数为空就返回false
     * @param objs 参数
     */
    public static Boolean isNotEmpty(Object ...objs) {
        for (Object obj : objs) {
            if (isEmpty(obj))
                return false;
        }
        return true;
    }

    /**
     * 校验多个都为空，只要有一个参数不为空就返回false
     * @param objs 参数
     */
    public static Boolean isEmpty(Object ...objs) {
        for (Object obj : objs) {
            if (isNotEmpty(obj))
                return false;
        }
        return true;
    }

    /**
     * 校验参数是否不为空
     * @param obj 参数
     */
    private static Boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    /**
     * 校验参数是否为空
     * @param obj 参数
     */
    private static Boolean isEmpty(Object obj) {
        if (obj instanceof String) {
            return StringUtils.isBlank((String) obj);
        }
        return null == obj;
    }

    /**
     * 校验参数是否为空
     * @param obj 参数
     * @param message 提示信息
     */
    public static void checkParam(Object obj, String message) {
        if (obj instanceof String) {
            if (StringUtils.isBlank((String) obj)) throw new RuntimeException(message);
        }
        Optional.ofNullable(obj).orElseThrow(() -> new RuntimeException(message));
    }

    /**
     * 校验javabean，传其他类型的对象可能会报错
     * @param obj  校验的javabean
     * @param nonEmptyFiledList 非空的属性集合，都可以为空传null
     * @param message  在为空的基础上，前面添加的提示信息，没有传null
     */
    public static void checkBeanByNonEmptyFiledList(Object obj, List<String> nonEmptyFiledList, String message) {
        checkBean(obj, nonEmptyFiledList, false, message);
    }

    /**
     * 校验javabean，传其他类型的对象可能会报错
     * @param obj  校验的javabean
     * @param emptyFiledList 为空的属性集合，都不可以为空传null
     * @param message  在为空的基础上，前面添加的提示信息，没有传null
     */
    public static void checkBeanByEmptyFiledList(Object obj, List<String> emptyFiledList, String message) {
        checkBean(obj, emptyFiledList, true, message);
    }

    /**
     * 校验javabean，所以的属性值都不能为空，传其他类型的对象可能会报错
     * @param obj 校验的javabean
     */
    public static void checkBean(Object obj) {
        checkBean(obj, null, null, null);
    }

    /**
     * 校验javabean，传其他类型的对象可能会报错
     * @param obj 校验的javabean
     * @param filedList 校验的属性集合，没有传null
     */
    public static void checkBean(Object obj, List<String> filedList) {
        checkBean(obj, filedList, null, null);
    }

    /**
     * 校验javabean，所以的属性值都不能为空，传其他类型的对象可能会报错
     * @param obj 校验的javabean
     * @param message  在为空的基础上，前面添加的提示信息，没有传null
     */
    public static void checkBean(Object obj, String message) {
        checkBean(obj, null, null, message);
    }

    /**
     * 校验javabean，传其他类型的对象可能会报错
     * @param obj  校验的javabean
     * @param filedList 校验的属性集合，没有传null
     * @param isEmptyFiledList 是否是空字段集合，默认true
     * @param message  在为空的基础上，前面添加的提示信息，没有传null
     */
    public static void checkBean(Object obj, List<String> filedList, Boolean isEmptyFiledList, String message) {
        checkParam(obj, (StringUtils.isBlank(message) ? "对象" : message) + "为空！");

        //传入为空属性集合为null表示当前集合所有属性都不能为空，但为了做校验，emptyFiledList也不能为null
        //在这里做校验可以减少http请求
        filedList = null != filedList ? filedList : new ArrayList<>();

        isEmptyFiledList = null != isEmptyFiledList ? isEmptyFiledList : true;

        List<Map> objectInfoList = getFiledsInfo(obj);

        for (Map objectInfo : objectInfoList) {
            String name = (String) objectInfo.get("name"); //对象属性名，属性名都是字符串

            //可以为空的属性不做校验
            if (filedList.contains(name) ^ isEmptyFiledList) {

                //校验对象属性值是否为空
                checkParam(objectInfo.get("value"), (StringUtils.isBlank(message) ? "对象的" : message + "的") + "【" + name + "】属性为空！");
            }
        }
    }

    /**
     * 获取属性类型(type)，属性名(name)，属性值(value)的map组成的list
     */
    public static List<Map> getFiledsInfo(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        List<Map> list = new ArrayList<>();
        Map<String, Object> infoMap = null;
        for (int i = 0; i < fields.length; i++) {
            infoMap = new HashMap<>();
            infoMap.put("type", fields[i].getType().toString());
            infoMap.put("name", fields[i].getName());
            infoMap.put("value", getFieldValueByName(fields[i].getName(), o));
            list.add(infoMap);
        }
        return list;
    }

    /**
     * 根据属性名获取属性值
     */
    public static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[]{});
            Object value = method.invoke(o, new Object[]{});
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println("(true ^ true) " + (true ^ true)); //false

        System.out.println("(true ^ false) " + (true ^ false)); //true

        //传入的集合中找到了元素，这个元素可以为空，不对这个元素做校验  预期false
        System.out.println("(true ^ true) " + (true ^ true)); //false

        //传入的集合中找到了元素，这个元素不可以为空，对这个元素做校验  预期true
        System.out.println("(true ^ false) " + (true ^ false)); //true

    }
}
