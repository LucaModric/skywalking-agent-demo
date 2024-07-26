/**
 * @Description: 拦截实例方法的顶层接口
 * @Author: tiger
 * @CreateDate: 2024/7/25 18:58
 */
package com.tiger.plugin.enhance;

import java.lang.reflect.Method;

public interface ClassInstanceInterceptor {

    /**
     * EnhanceInstance 的目的是为了在beforeMethod 和 afterMethod 之间实现参数的传递
     * 比如beforeMethod处理了一个Object,在afterMethod中要用到
     * @param target
     * @param method
     * @param allArguments
     * @param parameterTypes
     */
    void beforeMethod(EnhanceInstance target, Method method, Object[] allArguments, Class<?>[] parameterTypes);

    void afterMethod(EnhanceInstance target, Method method, Object[] allArguments, Class<?>[] parameterTypes, Object res);

    void handleEx(EnhanceInstance target, Method method, Object[] allArguments, Class<?>[] parameterTypes, Object res, Throwable t);
}
