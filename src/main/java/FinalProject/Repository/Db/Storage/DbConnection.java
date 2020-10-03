package FinalProject.Repository.Db.Storage;

import FinalProject.Exception.CannotConnectException;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Singleton
public class DbConnection {
    private DbConfigReader configReader;
    private Connection connection;

    @Inject
    public DbConnection(DbConfigReader configReader) {
        this.configReader = configReader;
    }

    /**
     * @return A database connection. Throws a StorageException if the connection cannot be obtained.
     */
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(configReader.getDbUrl(), configReader.getUser(), configReader.getPassword());
            }
        } catch (SQLException e) {
            throw new CannotConnectException(e.getMessage());
        }

        return connection;
    }
}
