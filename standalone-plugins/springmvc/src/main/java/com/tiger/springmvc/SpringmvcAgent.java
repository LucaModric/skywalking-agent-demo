/**
 * @Description:
 * @Author: tiger
 * @CreateDate: 2024/7/23 22:53
 */
package com.tiger.springmvc;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.agent.builder.AgentBuilder;

import java.lang.instrument.Instrumentation;

import static net.bytebuddy.matcher.ElementMatchers.isAnnotatedWith;
import static net.bytebuddy.matcher.ElementMatchers.named;


@Slf4j
public class SpringmvcAgent {

    private static final String REST_CONTROLLER_NAME = "org.springframework.web.bind.annotation.RestController";
    private static final String CONTROLLER_NAME = "org.springframework.stereotype.Controller";

    public static void premain(String args, Instrumentation inst) {
        log.info("springmvc agent");
        new AgentBuilder.Default()
                .type(isAnnotatedWith(named(REST_CONTROLLER_NAME).or(named(CONTROLLER_NAME))))
                .transform(new SpringmvcTransformer())
                .installOn(inst)
        ;

    }

}
