package sk.jacob.sql.ddl;

import sk.jacob.sql.Metadata;
import sk.jacob.sql.dialect.DialectVisitor;

public class Table extends DbObject {
    public final Column[] columns;

    public Table(String name, Metadata metadata,  Column ... columns) {
        super(name);
        this.columns = columns;
        for(Column column : columns) {
            column.setParentTable(this);
        }
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
    public DDLStatement create(DialectVisitor visitor) {
        return visitor.create(this);
    }
}
