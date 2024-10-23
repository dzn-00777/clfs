package org.earthdog.clfs.classloader;

import org.earthdog.clfs.stringcomplie.SourceJavaFileManager;

import java.io.InputStream;

/**
 * @Date 2024/10/20 17:04
 * @Author DZN
 * @Desc ByteCodeClassLoader
 */
public class ByteCodeClassLoader extends ClassLoader{

    private final ClassLoader classLoader;

    public ByteCodeClassLoader(String classNameOrGroupName, boolean isGroup) {
        if (isGroup) {
            this.classLoader = new GroupClassLoader();
        } else {
            this.classLoader = new SingleClassLoader();
        }
    }

    @Override
    protected Class<?> findClass(String name) {
        try {
            return classLoader.loadClass(name);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public InputStream getResourceAsStream(String name) {
        return classLoader.getResourceAsStream(name);
    }

    private static class SingleClassLoader extends ClassLoader {

    }

    private static class GroupClassLoader extends ClassLoader {


    }

}
