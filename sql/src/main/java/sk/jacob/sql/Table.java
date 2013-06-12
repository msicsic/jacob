package sk.jacob.sql;

import sk.jacob.sql.dialect.CompiledStatementList;
import sk.jacob.sql.dialect.DialectVisitor;

public class Table extends DbObject {
    public final String name;
    public final Column[] columns;

    public Table(Metadata metadata, String name, Column[] columns) {
        this.columns = columns;
        this.name = name;
        metadata.add(this);
    }

    @Override
    public String sql(DialectVisitor visitor) {
        return null; //visitor.visit(this);
    }
}
