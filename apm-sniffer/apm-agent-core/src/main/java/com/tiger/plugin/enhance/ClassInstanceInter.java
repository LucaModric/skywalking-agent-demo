/**
 * @Description:
 * @Author: tiger
 * @CreateDate: 2024/7/25 18:57
 */
package com.tiger.plugin.enhance;

import com.tiger.plugin.loader.InterceptorLoader;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

@Slf4j
public class ClassInstanceInter {

    private ClassInstanceInterceptor interceptor;

    public ClassInstanceInter(String methodsIntercept, ClassLoader classLoader) {
        try {
            interceptor = InterceptorLoader.load(methodsIntercept, classLoader);
        } catch (Exception e) {
            log.error("can not load interceptor {}", methodsIntercept, e);
        }
    }

    @RuntimeType
    public Object intercept(
            @This Object obj,
            @Origin Method targetMethod,
            @AllArguments Object[] allArguments,
            @SuperCall Callable<?> zuper
    ) throws Exception {
        EnhanceInstance instance = (EnhanceInstance) obj;
        Object result = null;
        try {
            try {
                interceptor.beforeMethod(instance, targetMethod, allArguments, targetMethod.getParameterTypes());
            } catch (Exception e) {
                log.error("obj {} before exe static method {} intercept failure", obj, targetMethod.getName(), e);
            }
            result = null;
            result = zuper.call();
            try {
                interceptor.afterMethod(instance, targetMethod, allArguments, targetMethod.getParameterTypes(), result);
            } catch (Exception e) {
                log.error("obj {} after exe static method {} intercept failure", obj, targetMethod.getName(), e);
            }
        } catch (Exception e) {
            try {
                interceptor.handleEx(instance, targetMethod, allArguments, targetMethod.getParameterTypes(), result, e);
            } catch (Exception e1) {
                log.error("obj {} handleEx exe static method {} intercept failure", obj, targetMethod.getName(), e);
            }
            throw e;
        }
        return result;
    }
}
