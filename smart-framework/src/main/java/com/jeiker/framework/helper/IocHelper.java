package com.jeiker.framework.helper;

import com.jeiker.framework.annotation.Inject;
import com.jeiker.framework.util.ArrayUtil;
import com.jeiker.framework.util.CollectionUtil;
import com.jeiker.framework.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 依赖注入助手类
 * 获取所有Bean Map，然后遍历这个映射关系，分别取出Bean 类与Bean 实例，
 * 进而通过反射获取类中所有的成员变量。遍历成员变量，判断是否带有Inject注解，
 * 如有，则根据Bean类取出的Bean实例，通过ReflectionUtil的setField方法来修改当前成员变量的值。
 *
 * @author huangyong
 * @since 1.0.0
 */
public final class IocHelper {

    static {
        // 获取所有的Bean类与 Bean 实例之间的映射关系（简称 Bean Map）
        Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
        if (CollectionUtil.isNotEmpty(beanMap)) {
            // 遍历 Bean Map
            for (Map.Entry<Class<?>, Object> beanEntry : beanMap.entrySet()) {
                // 从 BeanMap 中获取 Bean 类与 Bean 实例
                Class<?> beanClass = beanEntry.getKey();
                Object beanInstance = beanEntry.getValue();
                // 获取 Bean 类定义的所有成员变量（简称 Bean Field ）
                Field[] beanFields = beanClass.getDeclaredFields();
                if (ArrayUtil.isNotEmpty(beanFields)) {
                    // 遍历 Bean Field
                    for (Field beanField : beanFields) {
                        // 判断当前 Bean Field 是否带有 Inject 注解
                        if (beanField.isAnnotationPresent(Inject.class)) {
                            // 在 Bean Map 中获取 Bean Field 对应的实例
                            Class<?> beanFieldClass = beanField.getType();
                            Object beanFieldInstance = beanMap.get(beanFieldClass);
                            if (beanFieldInstance != null) {
                                // 通过反射初始化 BeanField 值
                                ReflectionUtil.setField(beanInstance, beanField, beanFieldInstance);
                            }
                        }
                    }
                }
            }
        }
    }
}
