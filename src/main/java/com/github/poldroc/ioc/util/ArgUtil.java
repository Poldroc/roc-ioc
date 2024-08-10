package com.github.poldroc.ioc.util;

import com.github.houbb.heaven.util.util.ArrayUtil;

public class ArgUtil {

    public static void notNull(Object object, String name) {
        if (null == object) {
            throw new IllegalArgumentException(name + " can not be null!");
        }
    }

    public static void notEmpty(String string, String name) {
        if (null == string || string.isEmpty()) {
            throw new IllegalArgumentException(name + " can not be null!");
        }
    }

    public static void notEmpty(Object[] array, String name) {
        if (ArrayUtil.isEmpty(array)) {
            throw new IllegalArgumentException(name + " excepted is not empty!");
        } else {
            Object[] var2 = array;
            int var3 = array.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                Object object = var2[var4];
                notNull(object, name + " element ");
            }

        }
    }
}
