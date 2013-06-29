package sk.jacob.sql.ddl;

import sk.jacob.sql.dialect.DialectVisitor;
import sk.jacob.sql.engine.DbEngine;

import java.sql.Connection;
import java.sql.Statement;

public abstract class DbObject {
    public final String name;

    protected DbObject(String name) {
        this.name = name;
    }

    public void create(DbEngine engine) {
        DialectVisitor dialect = engine.getDialect();
        DDLStatement ddl = this.create(dialect);
        Connection connection = engine.getConnection();
        try {
            Statement statement = connection.createStatement();
            statement.execute(ddl.inline);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DbEngine.close(connection);
        }
    }

    public void drop(DbEngine dbEngine) {}

    public abstract DDLStatement create(DialectVisitor dialect);
}
