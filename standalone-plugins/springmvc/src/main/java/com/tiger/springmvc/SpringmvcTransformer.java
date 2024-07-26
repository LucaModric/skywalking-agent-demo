/**
 * @Description:
 * @Author: tiger
 * @CreateDate: 2024/7/23 23:01
 */
package com.tiger.springmvc;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.utility.JavaModule;

import java.security.ProtectionDomain;

import static net.bytebuddy.matcher.ElementMatchers.isAnnotatedWith;
import static net.bytebuddy.matcher.ElementMatchers.nameEndsWith;
import static net.bytebuddy.matcher.ElementMatchers.nameStartsWith;

@Slf4j
public class SpringmvcTransformer implements AgentBuilder.Transformer {

    private static final String PREFIX = "org.springframework.web.bind.annotation";
    private static final String SUFFIX = "Mapping";

    @Override
    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, ProtectionDomain protectionDomain) {
        log.info("SpringmvcTransformer");
        return builder.method(isAnnotatedWith(nameStartsWith(PREFIX).and(nameEndsWith(SUFFIX))))
                .intercept(MethodDelegation.to(new SpringmvcIntercept()));
    }
}
