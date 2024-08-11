package com.github.poldroc.ioc.context;


import com.github.houbb.heaven.support.instance.impl.Instances;


import com.github.houbb.heaven.util.guava.Guavas;
import com.github.houbb.heaven.util.lang.StringUtil;
import com.github.houbb.heaven.util.util.ArrayUtil;
import com.github.poldroc.ioc.annotation.Bean;
import com.github.poldroc.ioc.annotation.Configuration;
import com.github.poldroc.ioc.annotation.Import;
import com.github.poldroc.ioc.constant.enums.BeanSourceTypeEnum;
import com.github.poldroc.ioc.constant.enums.ScopeEnum;
import com.github.poldroc.ioc.model.AnnotationBeanDefinition;
import com.github.poldroc.ioc.model.BeanDefinition;
import com.github.poldroc.ioc.model.impl.DefaultAnnotationBeanDefinition;
import com.github.poldroc.ioc.model.impl.DefaultBeanDefinition;
import com.github.poldroc.ioc.support.annotation.Lazys;
import com.github.poldroc.ioc.support.annotation.Scopes;
import com.github.poldroc.ioc.support.name.BeanNameStrategy;
import com.github.poldroc.ioc.support.name.impl.DefaultBeanNameStrategy;
import com.github.poldroc.ioc.util.ArgUtil;
import com.github.poldroc.ioc.util.ClassUtil;


import java.lang.reflect.Method;
import java.util.*;

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
        // configClasses 为配置类，需要加上类上的 @import 注解的类
        List<Class> configList = getConfigClassList();
        for (Class clazz : configList) {
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
     * 获取配置类列表信息
     * （1）这里首先需要进行一层过滤，避免重复导入。
     * （2）这里需要递归处理，处理 class 头上的 {@link Import} 配置信息。
     *
     * @return 配置类列表
     */
    private List<Class> getConfigClassList() {
        Set<Class> configSet = new HashSet<>(configClasses.length);

        for (Class configClass : configClasses) {
            // 添加每个配置类上的import注解的类
            addAllImportClass(configSet, configClass);
        }
        return new ArrayList<>(configSet);
    }

    /**
     * 添加所有导入配置信息
     *
     * @param configSet   配置集合
     * @param configClass 配置类
     */
    private void addAllImportClass(final Set<Class> configSet, final Class configClass) {
        configSet.add(configClass);

        if (configClass.isAnnotationPresent(Import.class)) {
            Import importInfo = (Import) configClass.getAnnotation(Import.class);

            Class[] importClasses = importInfo.value();
            if (importClasses != null && importClasses.length > 0) {
                for (Class importClass : importClasses) {
                    // 1. 循环添加
                    configSet.add(importClass);
                    // 2. 递归处理
                    addAllImportClass(configSet, importClass);
                }
            }
        }
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
        beanDefinition.setLazyInit(Lazys.getLazy(clazz));
        beanDefinition.setScope(Scopes.getScope(clazz));
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
                beanDefinition.setLazyInit(Lazys.getLazy(method));
                beanDefinition.setScope(Scopes.getScope(method));

                resultList.add(beanDefinition);
            }
        }
        return resultList;
    }
}
