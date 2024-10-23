package org.earthdog.clfs.metadata;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @Date 2024/10/17 13:38
 * @Author DZN
 * @Desc FileClassMetadata
 */
public class BinaryClassMetadata extends ClassMetadata<byte[]> {

    public BinaryClassMetadata(String qualifiedName, byte[] data) {
        super(qualifiedName, data);
    }

}
