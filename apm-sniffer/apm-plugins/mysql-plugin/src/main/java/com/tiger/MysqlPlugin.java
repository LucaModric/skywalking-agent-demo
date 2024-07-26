/**
 * @Description:
 * @Author: tiger
 * @CreateDate: 2024/7/25 19:11
 */
package com.tiger;

import com.tiger.plugin.intercept.ConstructorMethodsInterceptPoint;
import com.tiger.plugin.intercept.InstanceMethodsInterceptPoint;
import com.tiger.plugin.intercept.StaticMethodsInterceptPoint;
import com.tiger.plugin.match.ClassMatch;
import com.tiger.plugin.match.MultiNamedMatch;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;

import static net.bytebuddy.matcher.ElementMatchers.named;

public class MysqlPlugin extends AbstractClassEnhancePluginDefine {

    private static final String CLASS_NAME1 = "com.mysql.cj.jdbc.ClientPreparedStatement";
    private static final String CLASS_NAME2 = "com.mysql.cj.jdbc.ServerPreparedStatement";

    @Override
    protected ClassMatch enHanceClass() {
        return MultiNamedMatch.getInstance(CLASS_NAME1, CLASS_NAME2);
    }

    @Override
    protected InstanceMethodsInterceptPoint[] getInstanceMethodsInterceptPoint() {
        return new InstanceMethodsInterceptPoint[]{
                new InstanceMethodsInterceptPoint() {

                    @Override
                    public ElementMatcher<? super MethodDescription> getMethodsMatch() {
                        return named("execute")
                                .or(named("executeQuery")
                                        .or(named("executeUpdate")));
                    }

                    @Override
                    public String getMethodsIntercept() {
                        return MysqlInterCeptor.class.getName();
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
