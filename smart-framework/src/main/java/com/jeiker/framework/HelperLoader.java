package com.jeiker.framework;

import com.jeiker.framework.helper.*;
import com.jeiker.framework.util.ClassUtil;

/**
 * 加载相应的 Helper 类
 * 初始化框架，创建的几个Helper都需要通过一个入口程序来加载它们，实际上是加载它们的静态块。
 *
 * @author huangyong
 * @since 1.0.0
 */
public final class HelperLoader {

    public static void init() {
        Class<?>[] classList = {
            ClassHelper.class,
            BeanHelper.class,
            AopHelper.class,
            IocHelper.class,
            ControllerHelper.class
        };
        for (Class<?> cls : classList) {
            ClassUtil.loadClass(cls.getName());
        }
    }
}