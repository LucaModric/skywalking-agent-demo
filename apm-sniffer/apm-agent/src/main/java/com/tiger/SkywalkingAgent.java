/**
 * @Description:
 * @Author: tiger
 * @CreateDate: 2024/7/26 14:15
 */
package com.tiger;

import com.tiger.plugin.enhance.EnhanceInstanceContext;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.utility.JavaModule;

import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.List;

@Slf4j
public class SkywalkingAgent {

    public static void premain(String args, Instrumentation inst) {
        log.info("SkywalkingAgent entry");
        PluginFinder pluginFinder = new PluginFinder(new PluginBootstrap().loadPlugins());

        new AgentBuilder.Default()
                .type(pluginFinder.buildMatch())
                .transform(new Mytransform(pluginFinder))
                .installOn(inst);

    }

    private static class Mytransform implements AgentBuilder.Transformer {

        private PluginFinder pluginFinder;

        public Mytransform(PluginFinder pluginFinder) {
            this.pluginFinder = pluginFinder;
        }

        @Override
        public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, ProtectionDomain protectionDomain) {
            List<AbstractClassEnhancePluginDefine> list = pluginFinder.find(typeDescription);
            if (list == null || list.size() == 0) {
                return builder;
            }
            EnhanceInstanceContext conetxt = new EnhanceInstanceContext();
            DynamicType.Builder<?> newBuilder = builder;
            for (AbstractClassEnhancePluginDefine plugin : list) {
                DynamicType.Builder<?> possibleNewBuilder = plugin.define(newBuilder, typeDescription, classLoader, conetxt);
                if (possibleNewBuilder != null) {
                    newBuilder = possibleNewBuilder;
                }
            }
            if (conetxt.isEnhanced()) {
                log.info("{} 被增强了", typeDescription.getTypeName());
            }
            return newBuilder;
        }
    }
}
