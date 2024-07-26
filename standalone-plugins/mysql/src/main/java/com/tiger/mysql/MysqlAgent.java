package com.tiger.mysql;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.agent.builder.AgentBuilder;

import java.lang.instrument.Instrumentation;

import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 * Hello world!
 */
@Slf4j
public class MysqlAgent {

    private static final String CLASS_NAME1 = "com.mysql.cj.jdbc.ClientPreparedStatement";
    private static final String CLASS_NAME2 = "com.mysql.cj.jdbc.ServerPreparedStatement";

    public static void premain(String args, Instrumentation inst) {
        log.info("MysqlAgent");
        new AgentBuilder.Default()
                .type(named(CLASS_NAME1).or(named(CLASS_NAME2)))
                .transform(new Mysqltransform())
                .installOn(inst);

    }
}
