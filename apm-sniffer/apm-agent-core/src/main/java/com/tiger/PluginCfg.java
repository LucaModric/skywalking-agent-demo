/**
 * @Description:
 * @Author: tiger
 * @CreateDate: 2024/7/26 12:00
 */
package com.tiger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public enum PluginCfg {

    INSTANCE;

    List<PluginDefine> list = new ArrayList<>();

    void load(InputStream input) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // 处理每一行的内容
                if (line.trim().length() == 0 || line.startsWith("#")) {
                    continue;
                }
                PluginDefine pluginDefine = PluginDefine.build(line);
                list.add(pluginDefine);
            }
        }
    }

    public List<PluginDefine> getList() {
        return list;
    }
}
