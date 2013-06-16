package sk.jacob.sql;

import sk.jacob.sql.dialect.DialectVisitor;
import sk.jacob.sql.dialect.H2DialectVisitor;
import sk.jacob.sql.dialect.OracleDialectVisitor;
import sk.jacob.sql.dialect.PostgreDialectVisitor;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DbEngine {
    private final String driverClass;
    private final String url;
    private final String username;
    private final String password;

    private static final Map<String, DialectVisitor> dialects = new HashMap<String, DialectVisitor>();

    static {
        dialects.put("h2", new H2DialectVisitor());
        dialects.put("oracle", new OracleDialectVisitor());
        dialects.put("postgresql", new PostgreDialectVisitor());
    }

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

    public DialectVisitor getDialect() {
        return dialects.get(getDbVendorName(this.url));
    }

    private static String getDbVendorName(String url) {
        String[] parsedUrl = url.split(":");
        return parsedUrl[1];
    }
}
