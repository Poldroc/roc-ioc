package com.github.poldroc.ioc.context;

import com.github.houbb.json.bs.JsonBs;
import com.github.poldroc.ioc.core.impl.DefaultListableBeanFactory;
import com.github.poldroc.ioc.model.BeanDefinition;
import com.github.poldroc.ioc.model.impl.DefaultBeanDefinition;
import com.github.poldroc.ioc.util.ClassUtil;
import com.github.poldroc.ioc.util.FileUtil;

import java.io.InputStream;
import java.util.List;

/**
 * JSON 应用上下文
 *
 * @author Poldroc
 * @date 2024/7/31
 */

public class JsonApplicationContext extends DefaultListableBeanFactory {

    private final String fileName;

    public JsonApplicationContext(String fileName) {
        this.fileName = fileName;
        System.out.println(fileName);
        // 初始化配置
        this.init();
    }

    private void init() {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        String jsonConfig = FileUtil.getFileContent(is);
        List<DefaultBeanDefinition> beanDefinitions = JsonBs.deserializeArray(jsonConfig, DefaultBeanDefinition.class);
        if (beanDefinitions != null && !beanDefinitions.isEmpty()) {
            for (BeanDefinition beanDefinition : beanDefinitions) {
                super.registerBeanDefinition(beanDefinition.getName(), beanDefinition);
            }
        }
    }


}
