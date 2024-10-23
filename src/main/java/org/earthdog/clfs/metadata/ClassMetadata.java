package org.earthdog.clfs.metadata;

/**
 * @Date 2024/10/17 12:43
 * @Author DZN
 * @Desc ClassMetadata
 */
public abstract class ClassMetadata<T> {

    protected String qualifiedName;
    protected T data;
    protected ClassLoader classLoader;

    protected ClassMetadata(String qualifiedName, T data) {
        this.qualifiedName = qualifiedName;
        this.data = data;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public T getData() {
        return data;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }
}
