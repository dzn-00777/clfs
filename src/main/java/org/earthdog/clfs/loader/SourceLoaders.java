package org.earthdog.clfs.loader;

import org.earthdog.clfs.enums.DataSourceType;

import javax.sql.DataSource;

/**
 * @Date 2024/10/17 17:41
 * @Author DZN
 * @Desc SourceLoaders
 */
public final class SourceLoaders {

    public static SourceLoader newStringSourceLoader() {
        return new DefaultSourceLoader();
    }

    public static SourceLoader newDatabaseSourceLoader(DataSource dataSource) {
        return new DatabaseSourceLoader(dataSource, DataSourceType.MYSQL, new DefaultSourceLoader());
    }
}
