package org.earthdog.clfs.jdbc;

import org.earthdog.clfs.metadata.ClassMetadata;
import org.earthdog.clfs.metadata.ClassMetadataGroup;

import javax.sql.DataSource;
import java.sql.*;

/**
 * @Date 2024/10/20 14:42
 * @Author DZN
 * @Desc MysqlJdbcOp
 */
public class MysqlJdbcOp extends AbstractJdbcOps {

    private static final String SAVE_SQL = "insert into class_metadata (group_name, qualified_name) value (?, ?) on duplicate key update group_name = ?";
    private static final String SAVE_OVERFLOW_SQL = "insert into class_metadata_overflow (class_metadata_id, source_code, byte_code) value (?, ?, ?) on duplicate key update class_metadata_id = ?";
    private static final String GET_CLASS_META_ID = "select id from class_metadata where group_name = ? and qualified_name = ?";

    public MysqlJdbcOp(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void saveClassMetadata(ClassMetadata classMetadata) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement save = connection.prepareStatement(SAVE_SQL);
            preparedStatementSaveHandle(save, classMetadata, classMetadata.getQualifiedName());
            save.executeUpdate();

            int id = getId(connection, classMetadata.getQualifiedName(), classMetadata.getQualifiedName());

            PreparedStatement saveOver = connection.prepareStatement(SAVE_OVERFLOW_SQL);
            preparedStatementSaveOverHandle(saveOver, classMetadata, id);
            saveOver.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveClassMetadataGroup(ClassMetadataGroup classMetadataGroup) {
        String groupName = classMetadataGroup.getGroupName();
        ClassMetadata[] metadataArray = classMetadataGroup.getClassMetadataArray();
        try (Connection connection = dataSource.getConnection()) {
            for (ClassMetadata classMetadata : metadataArray) {
                PreparedStatement save = connection.prepareStatement(SAVE_SQL);
                preparedStatementSaveHandle(save, classMetadata, groupName);
                save.executeUpdate();

                int id = getId(connection, groupName, classMetadata.getQualifiedName());

                PreparedStatement saveOver = connection.prepareStatement(SAVE_OVERFLOW_SQL);
                preparedStatementSaveOverHandle(saveOver, classMetadata, id);
                saveOver.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void preparedStatementSaveHandle(PreparedStatement statement, ClassMetadata classMetadata, String groupName) throws SQLException {
        String qualifiedName = classMetadata.getQualifiedName();
        statement.setString(1, groupName);
        statement.setString(2, qualifiedName);
        statement.setString(3, groupName);
    }

    private void preparedStatementSaveOverHandle(PreparedStatement saveOver, ClassMetadata classMetadata, int id) throws SQLException {
        saveOver.setInt(1, id);
        saveOver.setString(2, classMetadata.getCode());
        saveOver.setBytes(3, classMetadata.getByteCode());
        saveOver.setInt(4, id);
    }

    private int getId(Connection connection, String groupName, String qualifiedName) throws SQLException {
        PreparedStatement idStatement = connection.prepareStatement(GET_CLASS_META_ID);
        idStatement.setString(1, groupName);
        idStatement.setString(2, qualifiedName);
        ResultSet resultSet = idStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt(1);
    }
}
