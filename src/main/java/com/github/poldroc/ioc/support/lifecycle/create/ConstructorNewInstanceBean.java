package com.github.poldroc.ioc.support.lifecycle.create;

import com.github.houbb.heaven.support.tuple.impl.Pair;
import com.github.poldroc.ioc.core.BeanFactory;
import com.github.poldroc.ioc.model.BeanDefinition;
import com.github.poldroc.ioc.model.ConstructorArgDefinition;
import com.github.poldroc.ioc.support.converter.StringValueConverterFactory;
import com.github.poldroc.ioc.util.ClassUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

/**
 * 根据构造器创建对象
 *
 * @author Poldroc
 * @date 2024/8/4
 */

public class ConstructorNewInstanceBean extends AbstractNewInstanceBean {

    private static final ConstructorNewInstanceBean INSTANCE = new ConstructorNewInstanceBean();

    /**
     * 获取单例实例
     *
     * @return 单例
     */
    public static ConstructorNewInstanceBean getInstance() {
        return INSTANCE;
    }


    /**
     * 创建对象实例
     * （1）获取所有的构造器列表{@link Class#getConstructors()}
     * （2）根据{@link BeanDefinition}指定的信息列表处理，选择精确匹配的构造器
     * （3）反射调用构造器创建对象实例。{@link java.lang.reflect.Constructor#newInstance(Object...)}
     * （3.1）反射的时候注意构造器的访问级别。
     *
     * @param beanFactory    对象工厂
     * @param beanDefinition 对象定义信息
     * @param beanClass      类信息
     * @return
     */
    @Override
    protected Optional<Object> newInstanceOpt(BeanFactory beanFactory, BeanDefinition beanDefinition, Class beanClass) {
        List<ConstructorArgDefinition> constructorArgList = beanDefinition.getConstructorArgList();

        // 无参构造器
        if (constructorArgList.isEmpty()) {
            return newInstanceOptWithoutArgs(beanClass);
        }

        // 根据指定参数信息获取对应构造器
        return newInstanceOptWithArgs(beanFactory, beanClass, constructorArgList);
    }

    /**
     * 创建无参构造器的对象实例
     *
     * @param beanClass 类信息
     * @return 实例结果
     */
    private Optional<Object> newInstanceOptWithoutArgs(Class beanClass) {
        return Optional.of(ClassUtil.newInstance(beanClass));
    }

    /**
     * 创建有参构造器的对象实例
     *
     * @param beanFactory        对象工厂
     * @param beanClass          类信息
     * @param constructorArgList 构造器参数列表
     * @return 实例结果
     */
    private Optional<Object> newInstanceOptWithArgs(BeanFactory beanFactory, Class beanClass, List<ConstructorArgDefinition> constructorArgList) {
        // 获取构造器参数列表
        Pair<Class[], Object[]> pair = getParamsPair(beanFactory, constructorArgList);
        try {
            return Optional.of(ClassUtil.getConstructor(beanClass, pair.getValueOne())
                    .newInstance(pair.getValueTwo()));
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 构建构造器参数列表
     * TODO: 循环依赖问题
     *
     * @param beanFactory        对象工厂
     * @param constructorArgList 构造器参数列表
     * @return 构造器参数列表
     */
    private Pair<Class[], Object[]> getParamsPair(BeanFactory beanFactory, List<ConstructorArgDefinition> constructorArgList) {
        int size = constructorArgList.size();
        Class[] paramTypes = new Class[size];
        Object[] paramValues = new Object[size];
        for (int i = 0; i < size; i++) {
            ConstructorArgDefinition constructorArgDefinition = constructorArgList.get(i);
            String ref = constructorArgDefinition.getRef();
            // 如果是引用类型
            if (ref != null) {
                Class refType = beanFactory.getType(ref);
                Object refValue = beanFactory.getBean(ref);
                paramTypes[i] = refType;
                paramValues[i] = refValue;
            } else {
                // 如果是值类型
                Class valueType = ClassUtil.getClass(constructorArgDefinition.getType());
                String valueStr = constructorArgDefinition.getValue();
                Object value = StringValueConverterFactory.convertValue(valueStr, valueType);
                paramTypes[i] = valueType;
                paramValues[i] = value;
            }
        }
        return Pair.of(paramTypes, paramValues);
    }


}
