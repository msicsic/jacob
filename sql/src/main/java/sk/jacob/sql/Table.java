package sk.jacob.sql;

import sk.jacob.sql.dialect.DDLStatement;
import sk.jacob.sql.dialect.DialectVisitor;

import java.util.List;

public class Table extends DbObject {
    public final Column[] columns;

    public Table(String name, Metadata metadata,  Column ... columns) {
        super(name);
        this.columns = columns;
        metadata.add(this);
    }

    public Column getIdColumn() {
        Column idColumn = null;
        for(Column c : columns) {
            if (c.options.isPrimaryKey()) {
                idColumn = c;
                break;
            }
        }
        return idColumn;
    }

    @Override
    public DDLStatement sql(DialectVisitor visitor) {
        return visitor.visit(this);
    }
}
