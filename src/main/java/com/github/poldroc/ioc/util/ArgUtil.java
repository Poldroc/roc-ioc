package com.github.poldroc.ioc.util;

public class ArgUtil {

    public static void notEmpty(String string, String name) {
        if (null == string || string.isEmpty()) {
            throw new IllegalArgumentException(name + " can not be null!");
        }
    }
}
