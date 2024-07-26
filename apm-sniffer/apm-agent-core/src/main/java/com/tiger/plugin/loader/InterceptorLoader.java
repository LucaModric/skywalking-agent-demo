/**
 * @Description:  拦截器的类加载器
 * @Author: tiger
 * @CreateDate: 2024/7/26 13:14
 */
package com.tiger.plugin.loader;

public class InterceptorLoader {

    /**
     * 如果拦截器想要访问byte buddy的对象，那么拦截器的类加载器必须是byte buddy的类加载器或者子类加载器
     * 因为同一类加载器，或者父子类加载器之间的类才能互相访问，两个独立的类加载之间的类不能互相访问。
     * @param interceptorName
     * @param classLoader 这里传进来的是byte buddy的类加载器
     * @param <T>
     * @return
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <T> T load(String interceptorName, ClassLoader targetClassLoader) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        if (targetClassLoader == null) {
            targetClassLoader = InterceptorLoader.class.getClassLoader();
        }
        AgenClassLoader agenClassLoader = new AgenClassLoader(targetClassLoader);
        Class<?> aClass = Class.forName(interceptorName, true, targetClassLoader);
        Object o = aClass.newInstance();
        return (T) o;
    }
}
