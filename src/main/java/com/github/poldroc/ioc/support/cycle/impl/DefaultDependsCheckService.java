package com.github.poldroc.ioc.support.cycle.impl;

import com.github.poldroc.ioc.model.BeanDefinition;
import com.github.poldroc.ioc.model.ConstructorArgDefinition;
import com.github.poldroc.ioc.model.PropertyArgDefinition;
import com.github.poldroc.ioc.support.cycle.DependsCheckService;
import com.github.poldroc.ioc.util.ArgUtil;

import java.util.*;

/**
 * 依赖检查服务
 * <p>
 * （1）注册时，存入所以当前 bean 对应的所有依赖信息
 * （2）注册 bean 被哪些依赖
 *
 * @author Poldroc
 * @date 2024/8/9
 */

public class DefaultDependsCheckService implements DependsCheckService {


    /**
     * 对象依赖的 map
     * key 依赖 value
     */
    private Map<String, Set<String>> beanDependsOnMap = new HashMap<>();


    /**
     * 对象被哪些属性依赖
     * value 依赖 key
     */
    private Map<String, Set<String>> beanDependsByMap = new HashMap<>();

    /**
     * 注册依赖信息
     *
     * @param beanDefinitionList bean 定义信息
     */
    @Override
    public void registerBeanDefinitions(List<BeanDefinition> beanDefinitionList) {
        for (BeanDefinition beanDefinition : beanDefinitionList) {
            String beanName = beanDefinition.getName();

            // 获得当前 bean 的所有依赖集合
            Set<String> dependsOnSet = getDependsOnSet(beanDefinition);
            beanDependsOnMap.put(beanName, dependsOnSet);

            // 循环处理 dependsBy 集合，更新 dependsOnSet 中每个 bean 被当前 bean 依赖
            this.fillDependsByMap(dependsOnSet, beanName);
        }
    }


    /**
     * 构建 dependsBy map
     * dependsOnSet的每个bean被beanName依赖
     *
     * @param dependsOnSet 依赖集合
     * @param beanName     当前 beanName
     */
    private void fillDependsByMap(Set<String> dependsOnSet, String beanName) {
        if (dependsOnSet == null || dependsOnSet.isEmpty()) {
            return;
        }
        for (String dependsOn : dependsOnSet) {
            Set<String> dependsBySet = beanDependsByMap.get(dependsOn);
            if (dependsBySet == null) {
                dependsBySet = new HashSet<>();
            }
            dependsBySet.add(beanName);
            beanDependsByMap.put(dependsOn, dependsBySet);
        }
    }

    /**
     * 获取对象的所有依赖集合
     * （1）构造器依赖 {@link ConstructorArgDefinition#getRef()}
     * （2）属性值依赖 {@link PropertyArgDefinition#getRef()}
     *
     * @param beanDefinition bean 定义
     * @return 依赖集合
     */
    private Set<String> getDependsOnSet(BeanDefinition beanDefinition) {
        Set<String> dependsSet = new HashSet<>();
        List<ConstructorArgDefinition> constructorArgList = beanDefinition.getConstructorArgList();
        if (constructorArgList != null && !constructorArgList.isEmpty()) {
            for (ConstructorArgDefinition constructorArgDefinition : constructorArgList) {
                String ref = constructorArgDefinition.getRef();
                if (ref != null && !ref.isEmpty()) {
                    dependsSet.add(ref);
                }
            }
        }

        List<PropertyArgDefinition> propertyArgList = beanDefinition.getPropertyArgList();
        if (propertyArgList != null && !propertyArgList.isEmpty()) {
            for (PropertyArgDefinition propertyArgDefinition : propertyArgList) {
                String ref = propertyArgDefinition.getRef();
                if (ref != null && !ref.isEmpty()) {
                    dependsSet.add(ref);
                }
            }
        }
        return dependsSet;
    }

    /**
     * 判断是否存在循环依赖
     *
     * @param beanName bean 名称
     * @return 是否存在循环依赖
     */
    @Override
    public boolean isCircleRef(String beanName) {
        ArgUtil.notEmpty(beanName, "beanName");
        // 获得当前 bean 的所有依赖集合
        Set<String> dependsOnSet = beanDependsOnMap.get(beanName);
        if (dependsOnSet == null || dependsOnSet.isEmpty()) {
            return false;
        }
        // 已有 beanName 依赖 dependsOnSet
        // 不能有 dependsOn 依赖 beanName
        for (String dependsOn : dependsOnSet) {
            if (isCircleRef(beanName, dependsOn, null)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 递归判单是否存在循环依赖
     * dependsOnBeanName 不能依赖 beanName
     * @param beanName          当前 bean 名称
     * @param dependsOnBeanName 依赖的 bean 名称
     * @param alreadySeen       已经检查过的 bean 列表
     * @return 是否存在循环依赖
     */
    private boolean isCircleRef(String beanName, String dependsOnBeanName, Set<String> alreadySeen) {
        // 当前 bean 已经检查过一次
        if (alreadySeen != null && !alreadySeen.isEmpty() && alreadySeen.contains(beanName)) {
            return false;
        }
        // 获得依赖当前 bean 的所有 bean
        Set<String> dependsBySet = beanDependsByMap.get(beanName);
        if (dependsBySet == null || dependsBySet.isEmpty()) {
            return false;
        }
        // 如果 依赖当前 bean 的所有 bean 中包含了 dependsOnBeanName，则存在循环依赖
        if (dependsBySet.contains(dependsOnBeanName)) {
            return true;
        }

        //  递归看 当前 bean 依赖的 bean (dependsOnBeanName) 是否依赖 依赖当前 bean 的 bean
        // beanName - 依赖 - > dependsOnBeanName
        //    ^                      |
        //    |                     |
        //   依赖 - dependsBy <- - 不能有依赖
        for (String dependsBy : dependsBySet) {
            if (alreadySeen == null) {
                alreadySeen = new HashSet<>();
            }
            alreadySeen.add(beanName);
            if (isCircleRef(dependsBy, dependsOnBeanName, alreadySeen)) {
                return true;
            }
        }

        return false;
    }
}
