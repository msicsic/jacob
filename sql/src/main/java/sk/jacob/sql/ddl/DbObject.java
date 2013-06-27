package sk.jacob.sql.ddl;

import sk.jacob.sql.dialect.DDLStatement;
import sk.jacob.sql.dialect.DialectVisitor;

public abstract class DbObject {
    public final String name;

    protected DbObject(String name) {
        this.name = name;
    }

    public abstract DDLStatement sql(DialectVisitor dialect);
}
