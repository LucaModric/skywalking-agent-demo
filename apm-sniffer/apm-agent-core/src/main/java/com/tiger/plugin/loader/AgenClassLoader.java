/**
 * @Description: 用于加载插件和插件的拦截器
 * @Author: tiger
 * @CreateDate: 2024/7/25 20:32
 */
package com.tiger.plugin.loader;

import com.tiger.PluginBootstrap;
import com.tiger.boot.AgentPackagePath;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;


@Slf4j
public class AgenClassLoader extends ClassLoader {

    /**
     * 自定义类加载器加载路径
     */
    private List<File> classpath;

    private List<Jar> allJars;

    private ReentrantLock reentrantLock = new ReentrantLock();

    /**
     * 加载插件相关类，除了拦截器
     */
    private static AgenClassLoader DEFAULT_LOADER;

    public AgenClassLoader(ClassLoader parent) {
        super();
        File agentJar = AgentPackagePath.getPath();
        classpath = new LinkedList<>();
        // 获取agentJar 父目录下的plugins目录中的插件jar文件
        classpath.add(new File(agentJar, "plugins"));
    }

    public static void initDefaultLoader() {
        if (DEFAULT_LOADER == null) {
            DEFAULT_LOADER = new AgenClassLoader(PluginBootstrap.class.getClassLoader());
        }
    }

    public static AgenClassLoader getDefaultLoader() {
        return DEFAULT_LOADER;
    }

    @Override
    protected Class<?> findClass(String className) throws ClassNotFoundException {
        List<Jar> allJars = getAllJars();
        String path = className.replaceAll("\\.", "/") + ".class";
        for (Jar jar : allJars) {
            ZipEntry entry = jar.jarFile.getEntry(path);
            if (entry == null) {
                continue;
            }
            try {
                /** 方式一
                 try (InputStream inputStream = jar.jarFile.getInputStream(entry);
                 ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                 // inputStream在这里处理输入流;outputStream例如读取数据 ;创建字节输出流
                 byte[] buffer = new byte[1024];  // 定义缓冲区大小为1024字节
                 int bytesRead = -1;
                 while ((bytesRead = inputStream.read(buffer)) != -1) {
                 // 将读取到的字节数据写入输出流
                 outputStream.write(buffer, 0, bytesRead);
                 }
                 byte[] byteArray = outputStream.toByteArray();
                 }*/

                // 方式二，使用jar协议
                URL url = new URL("jar:file:" + jar.fileSource.getAbsolutePath() + "!/" + path);
                byte[] bytes = IOUtils.toByteArray(url);
                return defineClass(className, bytes, 0, bytes.length);
            } catch (Exception e) {
                log.error("find class error {}", path, e);
            }
        }
        throw new ClassNotFoundException(className);
    }

    @Override
    public Enumeration<URL> getResources(String name) throws IOException {
        List<URL> allResources = new ArrayList<>();
        List<Jar> allJars = getAllJars();
        // getJarEntry 和 getEntry 啥区别
        for (Jar jar : allJars) {
            // 这里直接使用name是因为Resources目录下的文件打包直接在classpath路径下，不像class文件还有包名路径
            ZipEntry entry = jar.jarFile.getJarEntry(name);
            if (entry == null) {
                continue;
            }
            URL url = new URL("jar:file:" + jar.fileSource.getAbsolutePath() + "!/" + name);
            allResources.add(url);
        }

        Iterator<URL> iterator = allResources.iterator();
        return new Enumeration<URL>() {
            @Override
            public boolean hasMoreElements() {
                return iterator.hasNext();
            }

            @Override
            public URL nextElement() {
                return iterator.next();
            }
        };
    }


    @Override
    public URL getResource(String name) {
        URL url = null;
        List<Jar> allJars = getAllJars();
        for (Jar jar : this.allJars) {
            // getJarEntry 和 getEntry 啥区别
            ZipEntry entry = jar.jarFile.getJarEntry(name);
            if (entry != null) {
                continue;
            }
            try {
                url = new URL("jar:file:" + jar.fileSource.getAbsolutePath() + "!/" + name);
            } catch (MalformedURLException e) {
                log.error("load resurce error {}", name, e);
            }
        }
        return url;
    }

    @AllArgsConstructor
    private static class Jar {
        private final JarFile jarFile;
        private final File fileSource;

    }

    public List<Jar> getAllJars() {
        if (allJars == null) {
            try {
                reentrantLock.lock();
                if (allJars == null) {
                    allJars = doGetAllJars();
                }
            } catch (Exception e) {
                reentrantLock.unlock();
                log.error("load jar error", e);
            }
        }
        return allJars;
    }

    private List<Jar> doGetAllJars() throws IOException {
        List<Jar> list = new ArrayList<>();
        for (File path : classpath) {
            String[] jarFiles = path.list((dir, name) -> name.endsWith(".jar"));
            if (ArrayUtils.isEmpty(jarFiles)) {
                continue;
            }
            for (String jarFile : jarFiles) {
                File jarSource = new File(path, jarFile);
                Jar jar = new Jar(new JarFile(jarSource), jarSource);
                list.add(jar);
            }

        }
        return list;
    }

    public static void main(String[] args) {
        File file = new File("F:\\当前\\运维");
        String[] list = file.list();
        for (String s : list) {
            System.out.println(s);
        }
    }
}
