package org.earthdog.clfs.metadata;

/**
 * @Date 2024/10/17 21:26
 * @Author DZN
 * @Desc ClassMetadataGroup
 */
public class ClassMetadataGroup<T> {
    private final String groupName;
    private final ClassMetadata<T>[] classMetadataArray;
    private ClassLoader classLoader;

    public ClassMetadataGroup(String groupName, ClassMetadata<T>[] classMetadataArray) {
        this.groupName = groupName;
        this.classMetadataArray = classMetadataArray;
    }

    public String getGroupName() {
        return groupName;
    }

    public ClassMetadata<T>[] getClassMetadataArray() {
        return classMetadataArray;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }
}
