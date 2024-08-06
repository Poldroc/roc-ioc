package com.github.poldroc.ioc.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Optional;

public class ReflectMethodUtil {

    public static Object invokeFactoryMethod(Class clazz, Method factoryMethod) {
        ArgUtil.notNull(clazz, "clazz");
        ArgUtil.notNull(factoryMethod, "factoryMethod");
        String methodName = factoryMethod.getName();
        Class<?>[] paramTypes = factoryMethod.getParameterTypes();
        if (paramTypes.length != 0) {
            throw new RuntimeException(methodName + " must be has no params.");
        } else if (!Modifier.isStatic(factoryMethod.getModifiers())) {
            throw new RuntimeException(methodName + " must be static.");
        } else {
            Class returnType = factoryMethod.getReturnType();
            if (!returnType.isAssignableFrom(clazz)) {
                throw new RuntimeException(methodName + " must be return " + returnType.getName());
            } else {
                return invoke((Object) null, factoryMethod);
            }
        }
    }

    public static Object invoke(Object instance, Method method, Object... args) {
        ArgUtil.notNull(method, "method");

        try {
            return method.invoke(instance, args);
        } catch (InvocationTargetException | IllegalAccessException var4) {
            ReflectiveOperationException e = var4;
            throw new RuntimeException(e);
        }
    }
}
