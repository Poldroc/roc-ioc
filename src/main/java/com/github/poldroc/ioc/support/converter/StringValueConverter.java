package com.github.poldroc.ioc.support.converter;

/**
 * 字符串值转换接口
 * @author Poldroc
 * @date 2024/8/5
 */

public interface StringValueConverter<T> {

    /**
     * 对字符串值进行转换
     * @param value 值
     * @return 转换后的结果
     */
    T convert(final String value);

}
