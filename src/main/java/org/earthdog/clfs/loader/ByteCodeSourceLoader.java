package org.earthdog.clfs.loader;

import org.earthdog.clfs.metadata.ClassMetadata;
import org.earthdog.clfs.metadata.ClassMetadataGroup;

/**
 * @Date 2024/10/17 17:44
 * @Author DZN
 * @Desc FileSourceLoader
 */
public class ByteCodeSourceLoader extends AbstractSourceLoader<byte[]> {

    public ByteCodeSourceLoader() {
    }

    @Override
    public Object loadClass(ClassMetadata<byte[]> classMetadata) {
        return null;
    }

    @Override
    public void loadClassBatch(ClassMetadataGroup<byte[]> classMetadataGroup) {

    }
}
