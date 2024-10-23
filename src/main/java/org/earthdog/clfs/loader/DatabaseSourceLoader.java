package org.earthdog.clfs.loader;

import org.earthdog.clfs.enums.DataSourceType;
import org.earthdog.clfs.jdbc.JdbcOp;
import org.earthdog.clfs.metadata.ClassMetadata;
import org.earthdog.clfs.metadata.ClassMetadataGroup;
import org.earthdog.clfs.metadata.StringClassMetadata;

import javax.sql.DataSource;

/**
 * @Date 2024/10/17 17:55
 * @Author DZN
 * @Desc SchemaSourceLoader
 */
public class DatabaseSourceLoader extends DefaultSourceLoader {

    private final JdbcOp jdbcOp;
    private final DefaultSourceLoader defaultSourceLoader;

    public DatabaseSourceLoader(DataSource dataSource, DataSourceType type, DefaultSourceLoader defaultSourceLoader) {
        this.defaultSourceLoader = defaultSourceLoader;
        this.jdbcOp = JdbcOp.getInstance(dataSource, type);
    }

    @Override
    public Object loadClass(ClassMetadata classMetadata) {
        Object o = defaultSourceLoader.loadClass(classMetadata);
        // 将ByteCode添加到ClassMetadata中
        StringClassMetadata stringClassMetadata = (StringClassMetadata) classMetadata;
        byte[] byteCode = defaultSourceLoader.getByteCode("common", classMetadata.getQualifiedName());
        stringClassMetadata.setByteCode(byteCode);
        // 插入或更新至数据库
        jdbcOp.saveClassMetadata(stringClassMetadata);
        return o;
    }

    @Override
    public void loadClassBatch(ClassMetadataGroup classMetadataGroup) {
        String groupName = classMetadataGroup.getGroupName();
        defaultSourceLoader.loadClassBatch(classMetadataGroup);
        // 将ByteCode添加到ClassMetadata中
        ClassMetadata[] metadataArray = classMetadataGroup.getClassMetadataArray();
        for (ClassMetadata classMetadata : metadataArray) {
            StringClassMetadata stringClassMetadata = (StringClassMetadata) classMetadata;
            stringClassMetadata.setByteCode(defaultSourceLoader.getByteCode(groupName, stringClassMetadata.getQualifiedName()));
        }
        // 插入或更新至数据库
        jdbcOp.saveClassMetadataGroup(classMetadataGroup);
    }
}
