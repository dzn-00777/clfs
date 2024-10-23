package org.earthdog.clfs.jdbc;

import org.earthdog.clfs.enums.DataSourceType;
import org.earthdog.clfs.metadata.ClassMetadata;
import org.earthdog.clfs.metadata.ClassMetadataGroup;

import javax.sql.DataSource;

/**
 * @Date 2024/10/20 14:31
 * @Author DZN
 * @Desc JdbcOp
 */
public interface JdbcOp {

    static JdbcOp getInstance(DataSource dataSource, DataSourceType type) {
        if (type == DataSourceType.MYSQL) {
            return new MysqlJdbcOp(dataSource);
        }
        throw new RuntimeException();
    }

    void saveClassMetadata(ClassMetadata stringClassMetadata);

    void saveClassMetadataGroup(ClassMetadataGroup classMetadataGroup);
}
