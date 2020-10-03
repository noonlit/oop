package FinalProject.Repository.Db.Storage;

import FinalProject.Application.Config;

/**
 * Obtains configuration values necessary for connecting to the database.
 */
public class DbConfigReader {
    /**
     * The config section where the database configurations are found.
     */
    private static final String CONFIG_SECTION = "db";
    private String user;
    private String password;
    private String dbUrl;

    /**
     * @return The URL for connecting to the application database.
     */
    public String getDbUrl() {
        if (dbUrl == null) {
            dbUrl = "jdbc:mysql://" + Config.getConfigValue(CONFIG_SECTION, "url");
        }

        return dbUrl;
    }

    /**
     * @return The database user.
     */
    public String getUser() {
        if (user == null) {
            user = Config.getConfigValue(CONFIG_SECTION, "user");
        }

        return user;
    }

    /**
     * @return The database user password.
     */
    public String getPassword() {
        if (password == null) {
            password = Config.getConfigValue(CONFIG_SECTION, "password");
        }

        return password;
    }
}
