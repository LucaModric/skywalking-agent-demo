/**
 * @Description:
 * @Author: tiger
 * @CreateDate: 2024/7/25 19:04
 */
package com.tiger;

import com.tiger.plugin.enhance.ClassInstanceInterceptor;
import com.tiger.plugin.enhance.EnhanceInstance;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

@Slf4j
public class MysqlInterCeptor implements ClassInstanceInterceptor {
    @Override
    public void beforeMethod(EnhanceInstance target, Method method, Object[] allArguments, Class<?>[] parameterTypes) {
        log.info("MysqlInterCeptor beforeMethod target:{},method:{} ", target, method.getName());

    }

    @Override
    public void afterMethod(EnhanceInstance target, Method method, Object[] allArguments, Class<?>[] parameterTypes, Object res) {
        log.info("MysqlInterCeptor afterMethod target:{},method:{},result:{} ", target, method.getName(),res);
    }

    @Override
    public void handleEx(EnhanceInstance target, Method method, Object[] allArguments, Class<?>[] parameterTypes, Object res, Throwable e) {
        log.error("MysqlInterCeptor afterMethod target:{},method:{} ", target, method.getName(), e);
    }
}

