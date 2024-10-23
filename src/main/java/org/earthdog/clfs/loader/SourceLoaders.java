package org.earthdog.clfs.loader;

import org.earthdog.clfs.enums.DataSourceType;

/**
 * @Date 2024/10/17 17:41
 * @Author DZN
 * @Desc SourceLoaders
 */
public final class SourceLoaders {

    public static SourceLoader<String> newStringSourceLoader() {
        return new StringSourceLoader();
    }

    public static SourceLoader<byte[]> newByteCodeSourceLoader() {
        return new ByteCodeSourceLoader();
    }

    public static SourceLoader<String> newDatabaseSourceLoader() {
        return new DatabaseSourceLoader(null, DataSourceType.MYSQL, new StringSourceLoader());
    }
}
