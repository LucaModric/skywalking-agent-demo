/**
 * @Description: 静态方法拦截器
 * @Author: tiger
 * @CreateDate: 2024/7/25 18:02
 */
package com.tiger.plugin.enhance;

import java.lang.reflect.Method;

public interface StaticMethodInterceptor {

    void beforeMethod(Class<?> cla, Method method, Object[] allArguments, Class<?>[] parameterTypes);

    void afterMethod(Class<?> cla, Method method, Object[] allArguments, Class<?>[] parameterTypes, Object res);

    void handleEx(Class<?> cla, Method method, Object[] allArguments, Class<?>[] parameterTypes, Object res, Throwable t);

}
