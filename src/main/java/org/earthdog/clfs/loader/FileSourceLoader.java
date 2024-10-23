package org.earthdog.clfs.loader;

import org.earthdog.clfs.conf.Config;
import org.earthdog.clfs.metadata.ClassMetadata;
import org.earthdog.clfs.metadata.ClassMetadataGroup;

import java.io.File;

/**
 * @Date 2024/10/20 13:22
 * @Author DZN
 * @Desc FileSourceLoader
 */
public class FileSourceLoader extends AbstractSourceLoader<File>{

    private final SourceLoader<byte[]> byteCodeSourceLoader;

    public FileSourceLoader(Config config) {
        this.byteCodeSourceLoader = new ByteCodeSourceLoader();
    }

    @Override
    public Object loadClass(ClassMetadata<File> classMetadata) {
        return null;
    }

    @Override
    public void loadClassBatch(ClassMetadataGroup<File> classMetadataGroup) {

    }
}
