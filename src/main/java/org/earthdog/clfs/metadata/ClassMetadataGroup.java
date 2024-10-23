package org.earthdog.clfs.metadata;

/**
 * @Date 2024/10/17 21:26
 * @Author DZN
 * @Desc ClassMetadataGroup
 */
public class ClassMetadataGroup {
    private final String groupName;
    private final ClassMetadata[] classMetadataArray;
    private ClassLoader classLoader;

    public ClassMetadataGroup(String groupName, ClassMetadata[] classMetadataArray) {
        this.groupName = groupName;
        this.classMetadataArray = classMetadataArray;
    }

    public String getGroupName() {
        return groupName;
    }

    public ClassMetadata[] getClassMetadataArray() {
        return classMetadataArray;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }
}
