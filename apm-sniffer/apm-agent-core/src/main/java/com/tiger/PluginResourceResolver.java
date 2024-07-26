/**
 * @Description:
 * @Author: tiger
 * @CreateDate: 2024/7/25 23:17
 */
package com.tiger;

import com.tiger.plugin.loader.AgenClassLoader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;


@Slf4j
public class PluginResourceResolver {
    public List<URL> gResources() {
        List<URL> urls = new ArrayList<>();
        try {
            Enumeration<URL> resources = AgenClassLoader.getDefaultLoader().getResources("skywalking-plugin-def");
            while (resources.hasMoreElements()) {
                URL pluginDefUrl = resources.nextElement();
                urls.add(pluginDefUrl);
                log.info("skywalking-plugin-def url :{}", pluginDefUrl);
            }
        } catch (IOException e) {
            log.error("load resources error", e);
        }
        return urls;
    }
}
