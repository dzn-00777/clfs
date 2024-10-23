package org.earthdog.clfs.loader;

import org.earthdog.clfs.enums.DataSourceType;

/**
 * @Date 2024/10/17 17:41
 * @Author DZN
 * @Desc SourceLoaders
 */
public final class SourceLoaders {

    public static SourceLoader newStringSourceLoader() {
        return new DefaultSourceLoader();
    }

    public static SourceLoader newDatabaseSourceLoader() {
        return new DatabaseSourceLoader(null, DataSourceType.MYSQL, new DefaultSourceLoader());
    }
}
