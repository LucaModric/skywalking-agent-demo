/**
 * @Description:
 * @Author: tiger
 * @CreateDate: 2024/7/24 18:36
 */
package com.tiger.plugin.intercept;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;

public interface InstanceMethodsInterceptPoint {

    /**
     * 拦截的方法
     * @return
     */
    ElementMatcher<? super MethodDescription> getMethodsMatch();

    /**
     * 获取拦截器
     * @return
     */
    String getMethodsIntercept();
}
