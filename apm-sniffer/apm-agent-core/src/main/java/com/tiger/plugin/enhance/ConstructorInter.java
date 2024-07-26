/**
 * @Description:
 * @Author: tiger
 * @CreateDate: 2024/7/25 18:38
 */
package com.tiger.plugin.enhance;

import com.tiger.plugin.loader.InterceptorLoader;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;

@Slf4j
public class ConstructorInter {

    private ConstructorInterceptor interceptor;

    public ConstructorInter(String methodsIntercept, ClassLoader classLoader) {
        try {
            interceptor = InterceptorLoader.load(methodsIntercept, classLoader);
        } catch (Exception e) {
            log.error("can not load interceptor {}", methodsIntercept, e);
        }
    }

    @RuntimeType
    public void intercept(
            @This Object obj,
            @AllArguments Object[] allArguments
    ) throws Exception {
        try {
            EnhanceInstance instance = (EnhanceInstance) obj;
            interceptor.onConstruct(instance, allArguments);
        } catch (Exception e) {
            log.error("ConstructorInter failure", e);
        }

    }
}
