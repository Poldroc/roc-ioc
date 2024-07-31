package com.github.poldroc.ioc.context;

import com.github.houbb.json.bs.JsonBs;
import com.github.poldroc.ioc.core.impl.DefaultBeanFactory;
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

public class JsonApplicationContext extends DefaultBeanFactory {

    private final String fileName;

    public JsonApplicationContext(String fileName) {
        this.fileName = fileName;

        // 初始化配置
        this.init();
    }

    private void init() {
        InputStream is = ClassUtil.currentClassLoader().getResourceAsStream(fileName);
        String jsonConfig = FileUtil.getFileContent(is);
        List<DefaultBeanDefinition> beanDefinitions = JsonBs.deserializeArray(jsonConfig, DefaultBeanDefinition.class);
        if (beanDefinitions != null && !beanDefinitions.isEmpty()) {
            for (DefaultBeanDefinition beanDefinition : beanDefinitions) {
                super.registerBeanDefinition(beanDefinition.getName(), beanDefinition);
            }
        }
    }


}
