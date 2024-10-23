package org.earthdog.clfs.stringcomplie;

import javax.tools.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.earthdog.clfs.loader.SourceLoader.DEFAULT_GROUP;

/**
 * @Date 2024/7/7 21:26
 * @Author DZN
 * @Desc StringOfJavaFileManager
 */
public class SourceJavaFileManager extends ForwardingJavaFileManager<StandardJavaFileManager> {

    private final ThreadLocal<String> groupName = new ThreadLocal<>();
    private final ThreadLocal<ClassLoader> classLoader = new ThreadLocal<>();
    private final Map<String, Map<String, JavaFileObject>> fileObjectMap = new ConcurrentHashMap<>();

    public SourceJavaFileManager(StandardJavaFileManager fileManager) {
        super(fileManager);
    }

    @Override
    public FileObject getFileForInput(Location location, String packageName, String relativeName) {
        String qualifiedName = packageName + "." + relativeName;
        return getJavaFileObject(qualifiedName);
    }

    /**
     * 这里是编译器返回的同(源)Java文件对象,替换为SourceJavaFileObject实现
     */
    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) {
        return getJavaFileObject(className);
    }

    private JavaFileObject getJavaFileObject(String className) {
        String name = groupName.get();
        return fileObjectMap.get(Objects.requireNonNullElse(name, DEFAULT_GROUP)).get(className);
    }

    public ClassLoader getClassLoader(String groupName) {
        Map<String, JavaFileObject> objectMap = fileObjectMap.get(groupName);
        if (objectMap == null) {
            return null;
        }
        SourceSimpleJavaFileObject object = (SourceSimpleJavaFileObject) objectMap.values().iterator().next();
        return object.getClassLoader();
    }

    /**
     * 这里覆盖原来的类加载器
     */
    @Override
    public ClassLoader getClassLoader(Location location) {
        return classLoader.get();
    }


    @Override
    public String inferBinaryName(Location location, JavaFileObject file) {
        if (file instanceof SourceSimpleJavaFileObject) {
            return file.getName();
        }
        return super.inferBinaryName(location, file);
    }

    /**
     * 自定义方法,用于添加和缓存待编译的源文件对象
     */
    public void addJavaFileObject(String qualifiedName, JavaFileObject javaFileObject) {
        fileObjectMap.put(DEFAULT_GROUP, Collections.singletonMap(qualifiedName, javaFileObject));
    }

    public void addJavaFileObjectByGroup(String groupName, Map<String, JavaFileObject> map) {
        Map<String, JavaFileObject> objectMap = fileObjectMap.get(groupName);
        if (objectMap != null) {
            objectMap.putAll(map);
        } else {
            fileObjectMap.put(groupName, map);
        }
    }

    public JavaFileObject getJavaFileObjByQualifiedName(String qualifiedName) {
        Map<String, JavaFileObject> map = fileObjectMap.get(DEFAULT_GROUP);
        return map.get(qualifiedName);
    }

    public JavaFileObject getJavaFileObjByGroup(String groupName, String qualifiedName) {
        Map<String, JavaFileObject> objectMap = fileObjectMap.get(groupName);
        return objectMap.get(qualifiedName);
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader.set(classLoader);
    }

    public void removeClassLoader() {
        this.classLoader.remove();
    }

    public void setGroup(String groupName, ClassLoader classLoader) {
        this.groupName.set(groupName);
        this.classLoader.set(classLoader);
    }

    public void removeGroup() {
        this.classLoader.remove();
        this.groupName.remove();
    }

}

