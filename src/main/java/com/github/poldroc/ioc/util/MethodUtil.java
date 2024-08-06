package com.github.poldroc.ioc.util;



import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodUtil {


    /***
     * 调用 setter 方法，进行设置值
     */
    public static void invokeSetterMethod(final Object instance,
                                          final String propertyName,
                                          final Object value) {
        ArgUtil.notNull(instance, "instance");
        ArgUtil.notNull(propertyName, "propertyName");
        if(value == null) {
            return;
        }

        final Class<?> clazz = instance.getClass();
        String setMethodName = "set"+ firstToUpperCase(propertyName);

        // 反射获取对应的方法
        final Class<?> paramType = value.getClass();
        try {
            Method method = clazz.getMethod(setMethodName, paramType);
            method.invoke(instance, value);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static String firstToUpperCase(String str) {
        if (str != null && str.trim().length() != 0) {
            return str.length() == 1 ? str.toUpperCase() : str.substring(0, 1).toUpperCase() + str.substring(1);
        } else {
            return str;
        }
    }
}
