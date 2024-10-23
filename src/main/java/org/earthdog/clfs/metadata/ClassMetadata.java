package org.earthdog.clfs.metadata;

/**
 * @Date 2024/10/17 12:43
 * @Author DZN
 * @Desc ClassMetadata
 */
public abstract class ClassMetadata {

    protected String qualifiedName;
    protected String code;
    protected ClassLoader classLoader;

    protected ClassMetadata(String qualifiedName, String code) {
        this.qualifiedName = qualifiedName;
        this.code = code;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public String getCode() {
        return code;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }
}
