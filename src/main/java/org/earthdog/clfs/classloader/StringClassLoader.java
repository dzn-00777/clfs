package org.earthdog.clfs.classloader;

import org.earthdog.clfs.stringcomplie.SourceJavaFileManager;
import org.earthdog.clfs.stringcomplie.SourceSimpleJavaFileObject;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * @Date 2024/10/20 14:58
 * @Author DZN
 * @Desc StringClassLoader
 */
public class StringClassLoader extends ClassLoader {

    private final SourceJavaFileManager fileManager;
    private final String groupName;

    public StringClassLoader(SourceJavaFileManager fileManager, String groupName) {
        super(DefaultGroupClassLoader.getInstance(fileManager));
        this.fileManager = fileManager;
        this.groupName = groupName;
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        synchronized (getClassLoadingLock(name)) {
            Class<?> c = findLoadedClass(name);
            if (c != null) {
                return c;
            }
            SourceSimpleJavaFileObject javaFileObject = (SourceSimpleJavaFileObject) fileManager.getJavaFileObjByGroup(groupName, name);
            if (javaFileObject != null) {
                return findClass(name);
            }
            return getParent().loadClass(name);
        }
    }

    @Override
    protected Class<?> findClass(String name) {
        SourceSimpleJavaFileObject javaFileObject = (SourceSimpleJavaFileObject) fileManager.getJavaFileObjByGroup(groupName, name);
        byte[] byteCode = javaFileObject.getByteCode();
        return defineClass(name, byteCode, 0, byteCode.length);
    }

    public static class DefaultGroupClassLoader extends ClassLoader {

        private final SourceJavaFileManager fileManager;
        private static volatile DefaultGroupClassLoader DEFAULT_GROUP_CLASS_LOADER;

        private DefaultGroupClassLoader(SourceJavaFileManager fileManager) {
            super();
            this.fileManager = fileManager;
        }

        public static DefaultGroupClassLoader getInstance(SourceJavaFileManager fileManager) {
            if (DEFAULT_GROUP_CLASS_LOADER == null) {
                synchronized (DefaultGroupClassLoader.class) {
                    if (DEFAULT_GROUP_CLASS_LOADER == null) {
                        DEFAULT_GROUP_CLASS_LOADER = new DefaultGroupClassLoader(fileManager);
                    }
                }
            }
            return DEFAULT_GROUP_CLASS_LOADER;
        }

        @Override
        protected Class<?> findClass(String name) {
            SourceSimpleJavaFileObject javaFileObject = (SourceSimpleJavaFileObject) fileManager.getJavaFileObjByQualifiedName(name);
            byte[] byteCode = javaFileObject.getByteCode();
            return defineClass(name, byteCode, 0, byteCode.length);
        }

    }

}
