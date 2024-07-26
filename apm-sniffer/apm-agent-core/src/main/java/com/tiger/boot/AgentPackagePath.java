/**
 * @Description:
 * @Author: tiger
 * @CreateDate: 2024/7/25 20:47
 */
package com.tiger.boot;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.net.URL;

@Slf4j
public class AgentPackagePath {

    private static File AGENT_PACKAGE_PATH;

    public static File getPath() {
        if (AGENT_PACKAGE_PATH == null) {
            AGENT_PACKAGE_PATH = findPath();
        }
        return AGENT_PACKAGE_PATH;
    }

    private static File findPath() {
        String classResourcePath = AgentPackagePath.class.getName().replaceAll("\\.", "/") + ".class";
        // 分两种情况，idea里是以file开头，打成jar后的是jar:file:开头

        // file:/H:/projects/skywalking-agent-demo/apm-sniffer/apm-agent-core/target/classes/com/tiger/boot/AgentPackagePath.class
        // jar:file:XXX.jar!YYY.class  XXX表示jar的绝对路径，YYY表示class的绝对路径
        URL resource = ClassLoader.getSystemClassLoader().getResource(classResourcePath);
        File agentJarFile = null;
        if (resource != null) {
            String url = resource.toString();
            log.info("the class location is {}", url);
            boolean isInJar = url.indexOf("!") > -1;
            if (isInJar) {
                String subStr = StringUtils.substringBetween("file:", "!");
                agentJarFile = new File(subStr);
            }
            if (agentJarFile.exists()) {
                return agentJarFile.getParentFile();
            }
        }
        return agentJarFile;
    }

    public static void main(String[] args) {
        File file = new File("F:\\当前\\运维\\Docker容器技术与运维");
        System.out.println(file.getParentFile());
    }
}
