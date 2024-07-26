/**
 * @Description: 匹配哪些类的顶层接口
 * @Author: tiger
 * @CreateDate: 2024/7/24 18:29
 */
package com.tiger;

import com.tiger.plugin.enhance.ClassInstanceInter;
import com.tiger.plugin.enhance.ConstructorInter;
import com.tiger.plugin.enhance.EnhanceInstance;
import com.tiger.plugin.enhance.EnhanceInstanceContext;
import com.tiger.plugin.enhance.StaticMethodInter;
import com.tiger.plugin.intercept.ConstructorMethodsInterceptPoint;
import com.tiger.plugin.intercept.InstanceMethodsInterceptPoint;
import com.tiger.plugin.intercept.StaticMethodsInterceptPoint;
import com.tiger.plugin.match.ClassMatch;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.jar.asm.Opcodes;

import static net.bytebuddy.matcher.ElementMatchers.isStatic;
import static net.bytebuddy.matcher.ElementMatchers.not;

@Slf4j
public abstract class AbstractClassEnhancePluginDefine {

    private static final String CONTEXT_ATTR_NAME = "skyWalkingDynamicFied";

    /**
     * 需要拦截的类
     *
     * @return
     */
    protected abstract ClassMatch enHanceClass();

    protected abstract InstanceMethodsInterceptPoint[] getInstanceMethodsInterceptPoint();

    protected abstract StaticMethodsInterceptPoint[] getStaticMethodsInterceptPoint();

    protected abstract ConstructorMethodsInterceptPoint[] getConstructorMethodsInterceptPoint();


    public DynamicType.Builder<?> define(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, EnhanceInstanceContext conetxt) {
        String pulginDefineName = this.getClass().getName();
        String typeName = typeDescription.getTypeName();
        log.debug("开始使用{}增强{}", pulginDefineName, typeName);
        typeDescription.getActualName();
        builder = this.enHance(builder, typeDescription, classLoader, conetxt);
        // 放在这里并不准确，具体是否增强还需要看enHance方法，放在enHanceInstance里， 这里忽略掉这个细节
        conetxt.initializationStageCompleted();
        log.debug("使用{}增强{}结束", pulginDefineName, typeName);
        return builder;

    }

    private DynamicType.Builder<?> enHance(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, EnhanceInstanceContext conetxt) {
        builder = this.enHanceClass(builder, typeDescription, classLoader);
        builder = this.enHanceInstance(builder, typeDescription, classLoader, conetxt);
        return builder;
    }

    /**
     * 实例方法和构造方法增强
     *
     * @param builder
     * @param typeDescription
     * @param classLoader
     * @return
     */
    private DynamicType.Builder<?> enHanceInstance(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, EnhanceInstanceContext conetxt) {
        InstanceMethodsInterceptPoint[] ins = this.getInstanceMethodsInterceptPoint();
        ConstructorMethodsInterceptPoint[] con = this.getConstructorMethodsInterceptPoint();
        boolean existConPoint = false;
        if (con != null || con.length >= 0) {
            existConPoint = true;
        }

        boolean existInsPoint = false;
        if (ins != null || ins.length >= 0) {
            existInsPoint = true;
        }

        if (!existConPoint && !existInsPoint) {
            return builder;
        }


        /**
         * 新增属性
         * 同一个typeDescription只需要新增一次属性
         * 如何避免被执行多次
         * isAssignableTo 是XX的子类或实现类
         */
        if (!typeDescription.isAssignableTo(EnhanceInstance.class)) {
            if (!conetxt.isObjectExtended()) {
                builder = builder
                                                .defineField(CONTEXT_ATTR_NAME, Object.class, Opcodes.ACC_PRIVATE | Opcodes.ACC_VOLATILE)
                        // .defineField(CONTEXT_ATTR_NAME, Object.class, Modifier.PRIVATE | Modifier.VOLATILE)
                        // 指定get/set方法
                        .implement(EnhanceInstance.class)
                        .intercept(FieldAccessor.ofField(CONTEXT_ATTR_NAME))
                ;
                conetxt.objectExtendedCompleted();
            }
        }

        if (existConPoint) {
            for (ConstructorMethodsInterceptPoint point : con) {
                String methodsIntercept = point.getMethodsIntercept();
                if (methodsIntercept == null || methodsIntercept.trim().equals("")) {
                    throw new RuntimeException("要增强的类" + typeDescription.getTypeName() + "没有指定拦截器");
                }
                builder = builder.constructor(point.getMethodsMatch())
                        .intercept(SuperMethodCall.INSTANCE.andThen(MethodDelegation.withDefaultConfiguration().to(new ConstructorInter(methodsIntercept, classLoader))));

            }
        }

        if (existInsPoint) {
            for (InstanceMethodsInterceptPoint point : ins) {
                String methodsIntercept = point.getMethodsIntercept();
                if (methodsIntercept == null || methodsIntercept.trim().equals("")) {
                    throw new RuntimeException("要增强的类" + typeDescription.getTypeName() + "没有指定拦截器");
                }
                builder = builder.method(not(isStatic()).and(point.getMethodsMatch()))
                        .intercept(MethodDelegation.to(new ClassInstanceInter(methodsIntercept, classLoader)));
            }
        }
        return builder;

    }

    /**
     * 静态方法增强
     *
     * @param builder
     * @param typeDescription
     * @param classLoader
     * @return
     */
    private DynamicType.Builder<?> enHanceClass(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader) {
        StaticMethodsInterceptPoint[] staticMethodsInterceptPoint = this.getStaticMethodsInterceptPoint();
        if (staticMethodsInterceptPoint == null || staticMethodsInterceptPoint.length == 0) {
            return builder;
        }

        for (StaticMethodsInterceptPoint methodsInterceptPoint : staticMethodsInterceptPoint) {
            String intercept = methodsInterceptPoint.getMethodsIntercept();
            if (intercept == null || intercept.trim().equals("")) {
                throw new RuntimeException("没有指定拦截器");
            }
            builder = builder.method(isStatic().and(methodsInterceptPoint.getMethodsMatch())).intercept(MethodDelegation.to(new StaticMethodInter(intercept, classLoader)));
        }

        return builder;
    }


}
