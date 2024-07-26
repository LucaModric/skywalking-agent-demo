/**
 * @Description:
 * @Author: tiger
 * @CreateDate: 2024/7/23 23:04
 */
package com.tiger.springmvc;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

@Slf4j
public class SpringmvcIntercept {

    @RuntimeType
    public Object intercept(
            @This Object object,
            @Origin Method method,
            @AllArguments Object[] allArguments,
            @SuperCall Callable<?> zuper
    ) throws Exception {
        log.info("before springmvc agent,method name {}", method.getName());
        Object result = null;
        result = zuper.call();
        log.info("after springmvc agent,method name: {},result:{}", method.getName(), result);
        return result;
    }
}
