package org.earthdog.clfs.enums;

/**
 * @Date 2024/10/20 14:36
 * @Author DZN
 * @Desc DataSourceType
 */
public enum DataSourceType {

    MYSQL("mysql");

    DataSourceType(String value) {
        this.value = value;
    }

    private final String value;

    public String value() {
        return value;
    }
}
