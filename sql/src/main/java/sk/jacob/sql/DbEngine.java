package sk.jacob.sql;

import java.sql.*;

public class DbEngine {
    private final String driverClass;
    private final String url;
    private final String username;
    private final String password;

    public DbEngine(String driverClass, String url, String username, String password) {
        this.driverClass = driverClass;
        this.url = url;
        this.username = username;
        this.password = password;


        try {
            Class.forName(this.driverClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(this.url, this.username, this.password);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return connection;
    }
}
