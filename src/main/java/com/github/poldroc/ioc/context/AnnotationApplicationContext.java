package com.github.poldroc.ioc.context;


import com.github.houbb.heaven.support.instance.impl.Instances;


import com.github.houbb.heaven.util.lang.StringUtil;
import com.github.poldroc.ioc.annotation.Bean;
import com.github.poldroc.ioc.annotation.Configuration;
import com.github.poldroc.ioc.constant.enums.BeanSourceTypeEnum;
import com.github.poldroc.ioc.constant.enums.ScopeEnum;
import com.github.poldroc.ioc.model.AnnotationBeanDefinition;
import com.github.poldroc.ioc.model.BeanDefinition;
import com.github.poldroc.ioc.model.impl.DefaultAnnotationBeanDefinition;
import com.github.poldroc.ioc.model.impl.DefaultBeanDefinition;
import com.github.poldroc.ioc.support.name.BeanNameStrategy;
import com.github.poldroc.ioc.support.name.impl.DefaultBeanNameStrategy;
import com.github.poldroc.ioc.util.ArgUtil;
import com.github.poldroc.ioc.util.ClassUtil;


import java.lang.reflect.Method;
import java.util.ArrayList;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 基于注解的应用上下文
 *
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
     *
     * @param beanNameStrategy 命名策略
     */
    public void setBeanNameStrategy(BeanNameStrategy beanNameStrategy) {
        this.beanNameStrategy = beanNameStrategy;
    }

    @Override
    protected List<BeanDefinition> buildBeanDefinitionList() {
        List<BeanDefinition> resultList = new ArrayList<>();
        for (Class clazz : configClasses) {
            if (clazz.isAnnotationPresent(Configuration.class)) {
                Optional<AnnotationBeanDefinition> configurationOpt = buildConfigurationBeanDefinition(clazz);
                if (!configurationOpt.isPresent()) {
                    continue;
                }
                BeanDefinition beanDefinition = configurationOpt.get();
                resultList.add(beanDefinition);

                // 获取配置类中带有@Bean注解的definition
                List<AnnotationBeanDefinition> beanDefinitions = buildBeanAnnotationList(beanDefinition, clazz);
                resultList.addAll(beanDefinitions);

            }
        }
        return resultList;
    }

    /**
     * 构建配置对象定义
     *
     * @param clazz 类信息
     * @return 属性定义
     */
    private Optional<AnnotationBeanDefinition> buildConfigurationBeanDefinition(final Class clazz) {
        if (!clazz.isAnnotationPresent(Configuration.class)) {
            return Optional.empty();
        }

        Configuration configuration = (Configuration) clazz.getAnnotation(Configuration.class);
        String beanName = configuration.value();

        AnnotationBeanDefinition beanDefinition = new DefaultAnnotationBeanDefinition();
        beanDefinition.setClassName(clazz.getName());
        beanDefinition.setLazyInit(false);
        beanDefinition.setScope(ScopeEnum.SINGLETON.getCode());
        beanDefinition.setBeanSourceType(BeanSourceTypeEnum.CONFIGURATION);
        if (StringUtil.isEmpty(beanName)) {
            beanName = beanNameStrategy.generateBeanName(beanDefinition);
        }
        beanDefinition.setName(beanName);

        return Optional.of(beanDefinition);
    }

    /**
     * 构建 {@link Bean} 注解指定的方法对应的信息
     * <p>
     * （1）对象的结果怎么处理？结合 Config 进行 invoke 获取吗？
     * （2）如果是 lazy-init，那么肯定需要两样东西。方法签名+对应的依赖 config-instance。
     * 这两样可以考虑放在 AnnotationBeanDefinition 中。
     * （3）beanName 应该怎么获取？根据 methodName（这个） 还是根据 className？
     * 这个逻辑可以放在 {@link BeanNameStrategy} 中，保证逻辑的统一性。
     *
     * @param configuration 对象配置定义信息
     * @param clazz         类型信息
     * @return 结果列表
     */
    private List<AnnotationBeanDefinition> buildBeanAnnotationList(final BeanDefinition configuration, final Class clazz) {
        ArgUtil.notNull(clazz, "clazz");
        if (!clazz.isAnnotationPresent(Configuration.class)) {
            return Collections.emptyList();
        }

        List<AnnotationBeanDefinition> resultList = new ArrayList<>();
        List<Method> methodList = ClassUtil.getMethodList(clazz);
        for (Method method : methodList) {
            if (method.isAnnotationPresent(Bean.class)) {
                Bean bean = method.getAnnotation(Bean.class);
                String methodName = method.getName();
                Class<?> returnType = method.getReturnType();
                String beanName = bean.value();
                if (null == beanName || beanName.isEmpty()) {
                    beanName = methodName;
                }

                AnnotationBeanDefinition beanDefinition = new DefaultAnnotationBeanDefinition();
                beanDefinition.setName(beanName);
                beanDefinition.setClassName(returnType.getName());
                beanDefinition.setInitialize(bean.initMethod());
                beanDefinition.setDestroy(bean.destroyMethod());
                // 如何获取这个实例，反倒是可以直接通过方法调用。
                beanDefinition.setBeanSourceType(BeanSourceTypeEnum.CONFIGURATION_BEAN);
                beanDefinition.setConfigurationName(configuration.getName());
                beanDefinition.setConfigurationBeanMethod(methodName);

                // 这里后期需要添加 property/constructor 对应的实现
                beanDefinition.setLazyInit(false);
                beanDefinition.setScope(ScopeEnum.SINGLETON.getCode());

                resultList.add(beanDefinition);
            }
        }
        return resultList;
    }
}
