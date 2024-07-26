/**
 * @Description:
 * @Author: tiger
 * @CreateDate: 2024/7/24 16:41
 */
package com.tiger.mysql;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.security.ProtectionDomain;

import static net.bytebuddy.matcher.ElementMatchers.named;

@Slf4j
public class Mysqltransform implements AgentBuilder.Transformer {


    @Override
    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, ProtectionDomain protectionDomain) {
        log.info("Mysqltransform");
        return builder.method(named("execute")
                .or(named("executeQuery")
                        .or(named("executeUpdate"))))
                .intercept(MethodDelegation.to(new MysqlIntercept()));
    }
}
