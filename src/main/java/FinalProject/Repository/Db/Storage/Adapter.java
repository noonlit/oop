package FinalProject.Repository.Db.Storage;

import FinalProject.Domain.Entity.AbstractEntity;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Singleton
public class Adapter {
    private final DbConnection dbConnection;
    private final EntityTableMapper tableMapper;

    @Inject
    public Adapter(@NotNull DbConnection dbConnection, EntityTableMapper tableMapper) {
        this.dbConnection = dbConnection;
        this.tableMapper = tableMapper;
    }

    public Connection getConnection() {
        return dbConnection.getConnection();
    }

    /**
     * Returns the table name for the given entity.
     */
    public String getTableName(Class<? extends AbstractEntity> entityClass) {
        return tableMapper.getTableName(entityClass);
    }

    /**
     * Returns a built UPDATE statement based on the fields in the map.
     *
     * The fields map is in the column name => value to insert format.
     */
    public String buildUpdate(String tableName, @NotNull HashMap<String, Object> fields) {
        StringBuilder update = new StringBuilder("UPDATE " + tableName);

        // build prepared update statement like "SET field = ?"
        update.append(" SET ");
        for (Map.Entry<String, Object> field : fields.entrySet()) {
            String fieldName = field.getKey();
            update.append(fieldName).append(" = ?, ");
        }

        update.setLength(update.length() - 2);

        return update.toString();
    }

    /**
     * Returns a built INSERT statement based on the fields in the map.
     *
     * The fields map is in the column name => value to insert format.
     */
    public String buildInsert(String tableName, @NotNull HashMap<String, Object> fields) {
        StringBuilder insert = new StringBuilder("INSERT INTO " + tableName);

        // obtain the fields that have database column correspondents
        insert.append(" (");

        // build statement like "(column_1, column_2)"
        Set<String> columns = fields.keySet();
        for (String column : columns) {
            insert.append(column).append(", ");
        }
        insert.setLength(insert.length() - 2);
        insert.append(") ");

        // build prepared statment like "VALUES (?, ?, ?)"
        Collection<Object> values = fields.values();
        insert.append(" VALUES (");
        for (Object ignored : values) {
            insert.append("?, ");
        }

        insert.setLength(insert.length() - 2);
        insert.append(") ");

        return insert.toString();
    }

    /**
     * Binds the values in the given collection to the given SQL string and returns a prepared statement.
     */
    public PreparedStatement prepare(String sql, @NotNull Collection<?> valuesToBind) throws SQLException {

        PreparedStatement statement = getConnection().prepareStatement(sql);

        int index = 1;
        if (!valuesToBind.isEmpty()) {
            for (Object bind : valuesToBind) {
                statement.setObject(index, bind);
                index++;
            }
        }

        return statement;
    }

    /**
     * Binds a single value to the given SQL string and returns a prepared statement.
     */
    public PreparedStatement prepare(String sql, Object valueToBind) throws SQLException {

        PreparedStatement statement = getConnection().prepareStatement(sql);
        statement.setObject(1, valueToBind);

        return statement;
    }

    /**
     * Binds the values in the given collection to the given SQL string and returns a prepared statement.
     *
     * The statement will return the ID of the inserted row.
     */
    public PreparedStatement prepareInsert(String sql, @NotNull Collection<Object> valuesToBind) throws SQLException {

        PreparedStatement statement = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        int index = 1;
        if (!valuesToBind.isEmpty()) {
            for (Object bind : valuesToBind) {
                statement.setObject(index, bind);
                index++;
            }
        }

        return statement;
    }

    /**
     * Executes the query built by the given query builder.
     */
    public ResultSet executeBuiltQuery(@NotNull QueryBuilder queryBuilder) throws SQLException {
        PreparedStatement stmt = prepare(queryBuilder.getQuery(), queryBuilder.getBinds());
        return executeSQL(stmt);
    }

    /**
     * Executes a query representing a DML statement (e.g. INSERT, UPDATE, DELETE)
     */
    public int executeDML(@NotNull PreparedStatement query) throws SQLException {
        return query.executeUpdate();
    }

    /**
     * Executes a query (e.g. SELECT).
     */
    public ResultSet executeSQL(@NotNull PreparedStatement query) throws SQLException {
        return query.executeQuery();
    }
}
