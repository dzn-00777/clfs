package org.earthdog.clfs.jdbc;

import org.earthdog.clfs.metadata.ClassMetadata;
import org.earthdog.clfs.metadata.ClassMetadataGroup;
import org.earthdog.clfs.metadata.StringClassMetadata;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @Date 2024/10/20 14:42
 * @Author DZN
 * @Desc MysqlJdbcOp
 */
public class MysqlJdbcOp extends AbstractJdbcOps {

    private static final String SAVE_SQL = "insert into class_metadata (group_name, qualified_name, source_code, byte_code) value (?, ?, ?, ?) on duplicate key update group_name = ?";

    public MysqlJdbcOp(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void saveClassMetadata(StringClassMetadata stringClassMetadata) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SAVE_SQL);
            preparedStatementSaveHandle(statement, stringClassMetadata, stringClassMetadata.getQualifiedName());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveClassMetadataGroup(ClassMetadataGroup<String> classMetadataGroup) {
        String groupName = classMetadataGroup.getGroupName();
        ClassMetadata<String>[] metadataArray = classMetadataGroup.getClassMetadataArray();
        try (Connection connection = dataSource.getConnection()) {
        PreparedStatement statement = connection.prepareStatement(SAVE_SQL);
            for (ClassMetadata<String> classMetadata : metadataArray) {
                StringClassMetadata stringClassMetadata = (StringClassMetadata) classMetadata;
                preparedStatementSaveHandle(statement, stringClassMetadata, groupName);
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void preparedStatementSaveHandle(PreparedStatement statement, StringClassMetadata stringClassMetadata, String groupName) throws SQLException {
        String qualifiedName = stringClassMetadata.getQualifiedName();
        statement.setString(1, groupName);
        statement.setString(2, qualifiedName);
        statement.setString(3, stringClassMetadata.getData());
        statement.setBytes(4, stringClassMetadata.getByteCode());
        statement.setString(5, groupName);
    }
}
