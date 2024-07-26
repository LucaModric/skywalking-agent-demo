/**
 * @Description:
 * @Author: tiger
 * @CreateDate: 2024/7/24 16:42
 */
package com.tiger.mysql;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

@Slf4j
public class MysqlIntercept {

    @RuntimeType
    public Object intercept(
            // 实例方法必须写 @This，缺少这个入参就拦截不到实例方法了
            @This Object object,
            @Origin Method method,
            @AllArguments Object[] allArguments,
            @SuperCall Callable<?> zuper

    ) throws Exception {
        System.out.println("MysqlIntercept before method:" + method.getName());

        Object result = null;
        result = zuper.call();
        System.out.println("MysqlIntercept after method:" + method.getName() + " result: " + result);
        return result;
    }

}
