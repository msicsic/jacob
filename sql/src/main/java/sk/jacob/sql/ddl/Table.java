package sk.jacob.sql.ddl;

import sk.jacob.sql.Metadata;
import sk.jacob.sql.dialect.DialectVisitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Table extends DbObject {
    public final List<Column> columns = new ArrayList<Column>();

    public Table(String name, Metadata metadata) {
        super(name);
        metadata.add(this);
    }

    public Table(String name, Metadata metadata,  Column ... columns) {
        super(name);
        this.columns.addAll(Arrays.asList(columns));
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
