/**
 * @Description: 拦截构造方法的顶层接口
 * @Author: tiger
 * @CreateDate: 2024/7/25 18:46
 */
package com.tiger.plugin.enhance;

public interface ConstructorInterceptor {
    /**
     * 构造器执行后调用
     * @param obj  构造器new出来的对象
     * @param allAgrs
     */
    void onConstruct(EnhanceInstance obj, Object[] allAgrs);
}
