package com.github.poldroc.ioc.support.scanner.impl;

import com.github.houbb.heaven.util.guava.Guavas;
import com.github.houbb.heaven.util.util.ArrayUtil;
import com.github.poldroc.ioc.exception.IocRuntimeException;
import com.github.poldroc.ioc.model.BeanDefinition;
import com.github.poldroc.ioc.support.scanner.BeanDefinitionScanner;
import com.github.poldroc.ioc.util.ArgUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Set;

/**
 * Bean 定义扫描器
 *
 * @author Poldroc
 * @date 2024/8/10
 */

public class ClassPathBeanDefinitionScanner implements BeanDefinitionScanner {

    /**
     * 扫描指定的包，返回对应的 {@link BeanDefinition} 信息集合
     *
     * @param packageNames 包名称数组
     * @return 结果列表
     */
    @Override
    public Set<BeanDefinition> scan(final String... packageNames) {
        ArgUtil.notEmpty(packageNames, "packageNames");

        Set<BeanDefinition> definitionSet = Guavas.newHashSet();

        try {
            for (String packageName : packageNames) {
                String packageDirName = packageName.replace('.', '/');

                Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
                while (dirs.hasMoreElements()) {
                    URL url = dirs.nextElement();
                    String protocol = url.getProtocol();

                    // 文件处理
                    if ("file".equals(protocol)) {
                        String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                        File file = new File(filePath);
                        recursiveFile(packageName, file, definitionSet);
                    }

                    //jar 处理
                }
            }
        } catch (IOException e) {
            throw new IocRuntimeException(e);
        }

        return definitionSet;
    }

    /**
     * 递归处理文件信息
     *
     * @param packageNamePrefix 包名称前缀
     * @param file              根路径
     * @param definitionSet     定义的信息集合
     */
    private void recursiveFile(String packageNamePrefix,
                               final File file,
                               final Set<BeanDefinition> definitionSet) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            String dirName = file.getName();
            if (ArrayUtil.isNotEmpty(files)) {
                // 递归处理
                packageNamePrefix = packageNamePrefix + "." + dirName;

                for (File fileEntry : files) {
                    recursiveFile(packageNamePrefix, fileEntry, definitionSet);
                }
            }
        } else {
            // 比较简单的方式是获取对应的 class 全称。
            String fileName = file.getName().split("\\.")[0];
            String className = packageNamePrefix + "." + fileName;

            // 跳过特殊标志的信息
            // $ 这个怎么处理？
            // Proxy.isProxyClass(XXX)
            System.out.println("className：" + className);
        }
    }

    public static void main(String[] args) {
        String packageName = "com.github.poldroc.ioc";
        new ClassPathBeanDefinitionScanner().scan(packageName);
    }

}
