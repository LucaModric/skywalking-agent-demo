/**
 * @Description:
 * @Author: tiger
 * @CreateDate: 2024/7/26 12:01
 */
package com.tiger;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
@AllArgsConstructor
public class PluginDefine {

    private String name;

    private String pluginClassName;

    public static PluginDefine build(String cfg) {
        if (StringUtils.isEmpty(cfg) || !cfg.contains("=")) {
            throw new RuntimeException("配置文件格式错误：" + cfg);
        }
        String[] split = cfg.split("=");
        if (split.length != 2) {
            throw new RuntimeException("配置文件格式错误：" + cfg);
        }
        return new PluginDefine(split[0], split[1]);
    }
}
