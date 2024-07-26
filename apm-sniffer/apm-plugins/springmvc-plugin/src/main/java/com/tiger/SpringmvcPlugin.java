/**
 * @Description:
 * @Author: tiger
 * @CreateDate: 2024/7/24 19:07
 */
package com.tiger;

import com.tiger.plugin.intercept.ConstructorMethodsInterceptPoint;
import com.tiger.plugin.intercept.InstanceMethodsInterceptPoint;
import com.tiger.plugin.intercept.StaticMethodsInterceptPoint;
import com.tiger.plugin.match.ClassMatch;
import com.tiger.plugin.match.MultiClassAnnotationOrMatch;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;

import static net.bytebuddy.matcher.ElementMatchers.isAnnotatedWith;
import static net.bytebuddy.matcher.ElementMatchers.nameEndsWith;
import static net.bytebuddy.matcher.ElementMatchers.nameStartsWith;

public class SpringmvcPlugin extends AbstractClassEnhancePluginDefine {


    private static final String REST_CONTROLLER_NAME = "org.springframework.web.bind.annotation.RestController";
    private static final String CONTROLLER_NAME = "org.springframework.stereotype.Controller";

    private static final String PREFIX = "org.springframework.web.bind.annotation";
    private static final String SUFFIX = "Mapping";


    @Override
    protected ClassMatch enHanceClass()   {
        return MultiClassAnnotationOrMatch.getInstance(REST_CONTROLLER_NAME, CONTROLLER_NAME);
    }

    @Override
    protected InstanceMethodsInterceptPoint[] getInstanceMethodsInterceptPoint() {
        return new InstanceMethodsInterceptPoint[]{
                new InstanceMethodsInterceptPoint() {

                    @Override
                    public ElementMatcher<? super MethodDescription> getMethodsMatch() {
                        return isAnnotatedWith(nameStartsWith(PREFIX).and(
                                nameEndsWith(SUFFIX)
                        ));
                    }

                    @Override
                    public String getMethodsIntercept() {
                        return SpringmvcInterCeptor.class.getName();
                    }
                }
        };
    }

    @Override
    protected StaticMethodsInterceptPoint[] getStaticMethodsInterceptPoint() {
        return new StaticMethodsInterceptPoint[0];
    }

    @Override
    protected ConstructorMethodsInterceptPoint[] getConstructorMethodsInterceptPoint() {
        return new ConstructorMethodsInterceptPoint[0];
    }
}
