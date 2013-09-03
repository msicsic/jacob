package sk.jacob.sql.ddl;

import sk.jacob.sql.dialect.GenericDialectVisitor;
import sk.jacob.sql.engine.DbEngine;

public abstract class DbObject implements DDLClause {
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

    public String toString() {
        return dump();
    }

    public String dump() {
        DDLStatement statement  = this.create();
        StringBuffer sb = new StringBuffer(statement.inline);
        for(String outline : statement.outline) {
            sb.append(outline);
        }
        return sb.toString();
    };
}
