package sk.jacob.sql;

import sk.jacob.sql.dialect.DialectVisitor;
import sk.jacob.sql.dialect.H2DialectVisitor;
import sk.jacob.sql.dialect.OracleDialectVisitor;
import sk.jacob.sql.dialect.PostgreDialectVisitor;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DbEngine {
    public static enum DB_CONFIG {
        H2(new H2DialectVisitor(), "org.h2.Driver"),
        ORACLE(new OracleDialectVisitor(), "com.oracle.jdbc"),
        POSTGRESQL( new PostgreDialectVisitor(), "org.postgresql");

        public final DialectVisitor dialectVisitor;
        public final String driverClassName;

        DB_CONFIG(DialectVisitor dialectVisitor, String driverClassName) {
            this.dialectVisitor = dialectVisitor;
            this.driverClassName = driverClassName;
        }
    }

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

    public DbEngine(String url, String username, String password) {
        this(getDriverClassName(url), url, username, password);
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
        return getDialect(this.url);
    }

    public DialectVisitor getDialect(String url) {
        DB_CONFIG dbConfig = DB_CONFIG.valueOf(getDbVendorName(url).toUpperCase());
        return dbConfig.dialectVisitor;
    }

    private static String getDriverClassName(String url) {
        DB_CONFIG dbConfig = DB_CONFIG.valueOf(getDbVendorName(url).toUpperCase());
        return dbConfig.driverClassName;
    }

    private static String getDbVendorName(String url) {
        String[] parsedUrl = url.split(":");
        return parsedUrl[1];
    }
}
