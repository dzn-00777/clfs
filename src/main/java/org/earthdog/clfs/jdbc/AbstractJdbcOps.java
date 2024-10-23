package org.earthdog.clfs.jdbc;

import javax.sql.DataSource;

/**
 *  @Date   2024/10/20 14:45
 *  @Author DZN
 *  @Desc   AbstractJdbcOps
 */
public abstract class AbstractJdbcOps implements JdbcOp{
    protected final DataSource dataSource;
    protected AbstractJdbcOps(DataSource dataSource) {
        this.dataSource = dataSource;
    }

}
