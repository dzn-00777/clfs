package org.earthdog.clfs.init;

import org.earthdog.clfs.conf.Config;
import org.earthdog.clfs.enums.DataSourceType;

/**
 * @Date 2024/10/23 12:56
 * @Author DZN
 * @Desc DatabaseInitialization
 */
public abstract class DatabaseInitialization implements Initialization {

    protected Config config;

    protected DatabaseInitialization(Config config) {
        this.config = config;
    }

    public static DatabaseInitialization getInstance(DataSourceType type, Config config) {
        if (type == DataSourceType.MYSQL) {
            return new MysqlInitialization(config);
        }
        throw new RuntimeException();
    }

}
