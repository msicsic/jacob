package sk.jacob.sql;

import sk.jacob.sql.dialect.DDLStatement;
import sk.jacob.sql.dialect.DialectVisitor;

public class Table extends DbObject {
    public final Column[] columns;

    public Table(String name, Metadata metadata,  Column ... columns) {
        super(name);
        this.columns = columns;
        metadata.add(this);
    }

    @Override
    public DDLStatement sql(DialectVisitor visitor) {
        return visitor.visit(this);
    }
}
