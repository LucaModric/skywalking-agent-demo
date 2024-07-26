/**
 * @Description:
 * @Author: tiger
 * @CreateDate: 2024/7/25 17:53
 */
package com.tiger.plugin.enhance;

import com.tiger.plugin.loader.InterceptorLoader;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

@Slf4j
public class StaticMethodInter {


    private StaticMethodInterceptor interceptor;

    public StaticMethodInter(String interceptName, ClassLoader classLoader) {
        try {
            interceptor = InterceptorLoader.load(interceptName, classLoader);
        } catch (Exception e) {
            log.error("can not load interceptor {}", interceptName, e);
        }
    }


    @RuntimeType
    public Object intercept(
            @Origin Class<?> cla,
            @Origin Method targetMethod,
            @AllArguments Object[] allArguments,
            @SuperCall Callable<?> zuper
    ) throws Exception {
        Object result = null;
        try {
            try {
                interceptor.beforeMethod(cla, targetMethod, allArguments, targetMethod.getParameterTypes());
            } catch (Exception e) {
                log.error("class {} before exe static method {} intercept failure", cla, targetMethod.getName(), e);
            }
            result = null;
            result = zuper.call();
            try {
                interceptor.afterMethod(cla, targetMethod, allArguments, targetMethod.getParameterTypes(), result);
            } catch (Exception e) {
                log.error("class {} after exe static method {} intercept failure", cla, targetMethod.getName(), e);
            }
        } catch (Exception e) {
            try {
                interceptor.handleEx(cla, targetMethod, allArguments, targetMethod.getParameterTypes(), result, e);
            } catch (Exception e1) {
                log.error("class {} handleEx exe static method {} intercept failure", cla, targetMethod.getName(), e);
            }
            throw e;
        }
        return result;
    }
}
