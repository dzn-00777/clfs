package org.earthdog.clfs.loader;

import org.earthdog.clfs.conf.Config;
import org.earthdog.clfs.metadata.ClassMetadata;
import org.earthdog.clfs.metadata.ClassMetadataGroup;

import java.util.List;

/**
 * @Date 2024/10/17 12:41
 * @Author DZN
 * @Desc SourceLoader
 */
public interface SourceLoader {

    String JAVA_EXTENSION = ".java";
    String CLASS_OUTPUT_PATH = "C:\\Users\\28797\\Desktop\\classes\\";
    String SOURCE_PATH = "C:\\Users\\28797\\Desktop\\sourceJava\\";
    String DEFAULT_GROUP = "common";

    Object loadClass(ClassMetadata classMetadata);

    void loadClassBatch(ClassMetadataGroup classMetadataGroup);

    Config getConfig();

}
