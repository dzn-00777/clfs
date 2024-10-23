package org.earthdog.clfs.init;

import org.earthdog.clfs.enums.DataSourceType;

/**
 * @Date 2024/10/23 12:56
 * @Author DZN
 * @Desc DatabaseInitialization
 */
public interface DatabaseInitialization extends Initialization{

    static DatabaseInitialization getInstance(DataSourceType type) {
        if (type == DataSourceType.MYSQL) {
            return new MysqlInitialization();
        }
        throw new RuntimeException();
    }

}
