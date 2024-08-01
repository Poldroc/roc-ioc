package com.github.poldroc.ioc.test.core;

import com.github.poldroc.ioc.context.JsonApplicationContext;
import com.github.poldroc.ioc.core.ListableBeanFactory;
import com.github.poldroc.ioc.test.service.Person;
import org.junit.Test;

import java.util.List;

/**
 * bean 工厂测试类
 * @author Poldroc
 * @date 2024/8/1
 */

public class ListableBeanFactoryTest {

    /**
     * bean 工厂
     */
    private static final ListableBeanFactory BEAN_FACTORY = new JsonApplicationContext("persons.json");

    /**
     * 测试
     */
    @Test
    public void getBeansTest() {
        List<Person> personList = BEAN_FACTORY.getBeans(Person.class);
        personList.get(0).name();
        personList.get(1).name();
    }

}
