package com.github.poldroc.ioc.context;


import com.github.houbb.heaven.support.instance.impl.Instances;
import com.github.poldroc.ioc.annotation.Configuration;
import com.github.poldroc.ioc.constant.enums.ScopeEnum;
import com.github.poldroc.ioc.model.BeanDefinition;
import com.github.poldroc.ioc.model.impl.DefaultBeanDefinition;
import com.github.poldroc.ioc.support.name.BeanNameStrategy;
import com.github.poldroc.ioc.support.name.impl.DefaultBeanNameStrategy;
import com.github.poldroc.ioc.util.ArgUtil;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 基于注解的应用上下文
 * @author Poldroc
 * @date 2024/8/10
 */

public class AnnotationApplicationContext extends AbstractApplicationContext {

    /**
     * 配置类信息
     */
    private final Class[] configClasses;

    /**
     * 命名策略
     */
    private BeanNameStrategy beanNameStrategy = Instances.singleton(DefaultBeanNameStrategy.class);

    public AnnotationApplicationContext(Class... configClasses) {
        ArgUtil.notEmpty(configClasses, "configClasses");
        this.configClasses = configClasses;
        super.init();
    }

    /**
     * 设置属性名称命名策略
     * @param beanNameStrategy 命名策略
     */
    public void setBeanNameStrategy(BeanNameStrategy beanNameStrategy) {
        this.beanNameStrategy = beanNameStrategy;
    }

    @Override
    protected List<BeanDefinition> buildBeanDefinitionList() {
        List<BeanDefinition> beanDefinitionList = new ArrayList<>();
        for (Class clazz : configClasses) {
            if (clazz.isAnnotationPresent(Configuration.class)) {
                Configuration configuration = (Configuration)clazz.getAnnotation(Configuration.class);
                String beanName = configuration.value();
                BeanDefinition beanDefinition = DefaultBeanDefinition.newInstance();
                beanDefinition.setClassName(clazz.getName());
                beanDefinition.setLazyInit(false);
                beanDefinition.setScope(ScopeEnum.SINGLETON.getCode());
                if (beanName == null || beanName.isEmpty()) {
                    beanName = beanNameStrategy.generateBeanName(beanDefinition);
                }
                beanDefinition.setName(beanName);
                beanDefinitionList.add(beanDefinition);
            }
        }
        return beanDefinitionList;
    }
}
