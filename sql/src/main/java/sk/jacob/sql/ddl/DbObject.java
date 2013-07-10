package sk.jacob.sql.ddl;

import sk.jacob.sql.dialect.GenericDialectVisitor;
import sk.jacob.sql.engine.DbEngine;

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

    public DDLStatement drop() {
        return drop(GenericDialectVisitor.INSTANCE);
    }
    public DDLStatement drop(DbEngine dbEngine) {
        return drop(dbEngine.getDialect());
    }
}
