package sk.jacob.sql.ddl;

import sk.jacob.sql.dialect.DialectVisitor;
import sk.jacob.sql.dialect.GenericDialectVisitor;
import sk.jacob.sql.engine.DbEngine;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

public abstract class DbObject implements DDLEpression {
    public final String name;

    protected DbObject(String name) {
        this.name = name;
    }

    public DDLStatement create() {
        return create(GenericDialectVisitor.INSTANCE);
    }

    public DDLStatement create(DbEngine dbEngine) {
        return create(dbEngine.getDialect());
    }

    public void drop(DbEngine dbEngine) {}
}
