package com.Jasetol.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class Reflect {
    public static void reflectSetField(Object object,String fieldName,Object fieldValue) throws Exception{  // 通过反射对字段进行赋值
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object,fieldValue);
    }

    public static void reflectSetField(Class clazz,Object object,String fieldName,Object fieldValue) throws Exception{  // 通过反射对字段进行赋值
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object,fieldValue);
    }

    public static Object reflectGetObject(String className,Class[] paramsType,Object[] paramsValues) throws Exception{  // 通过反射获取对象
        Constructor constructor = Class.forName(className).getDeclaredConstructor(paramsType);
        constructor.setAccessible(true);
        return constructor.newInstance(paramsValues);
    }

    public static Object reflectGetField(Object object,String fieldName) throws Exception{  // 通过反射获取字段
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(object);
    }
}
