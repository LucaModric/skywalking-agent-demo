/**
 * @Description:
 * @Author: tiger
 * @CreateDate: 2024/7/25 20:33
 */
package com.tiger;

import com.tiger.plugin.loader.AgenClassLoader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PluginBootstrap {

    public List<AbstractClassEnhancePluginDefine> loadPlugins() {
        List<AbstractClassEnhancePluginDefine> plugins = new ArrayList<>();
        AgenClassLoader.initDefaultLoader();
        PluginResourceResolver pluginResourceResolver = new PluginResourceResolver();
        List<URL> list = pluginResourceResolver.gResources();
        if (list == null || list.size() == 0) {
            log.info("plugins not found");
            return plugins;
        }
        for (URL url : list) {
            try {
                PluginCfg.INSTANCE.load(url.openStream());
            } catch (IOException e) {
                log.error("init plugin def   {}  error ", url, e);
            }
        }

        List<PluginDefine> pluginDefines = PluginCfg.INSTANCE.getList();
        for (PluginDefine pluginDefine : pluginDefines) {
            try {
                Class<?> aClass = Class.forName(pluginDefine.getPluginClassName(), true, AgenClassLoader.getDefaultLoader());
                AbstractClassEnhancePluginDefine plugin = (AbstractClassEnhancePluginDefine) aClass.newInstance();
                plugins.add(plugin);
            } catch (Exception e) {
                log.error("load class {}  error ", pluginDefine.getPluginClassName(), e);
            }
        }
        return plugins;
    }
}
