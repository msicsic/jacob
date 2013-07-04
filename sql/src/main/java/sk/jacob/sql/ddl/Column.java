package sk.jacob.sql.ddl;

import sk.jacob.sql.dialect.DialectVisitor;

public class Column extends DbObject {
    public final TYPE.Type type;
    public final ColumnOptions options;
    private Table parentTable;

    public Column(String name, TYPE.Type type) {
        this(name, type, new ColumnOptions());
    }

    public Column(String name, TYPE.Type type, IColumnOptions options) {
        super(name);
        this.type = type;
        this.options = (ColumnOptions)options;
    }

    public Column(String name, TYPE.Type type, Table parentTable) {
        this(name, type, new ColumnOptions(), parentTable);
    }

    public Column(String name, TYPE.Type type, IColumnOptions options, Table parentTable) {
        super(name);
        this.type = type;
        this.options = (ColumnOptions)options;
        this.setParentTable(parentTable);
        parentTable.columns.add(this);
    }

    public static IColumnOptions options() {
        return new ColumnOptions();
    }

    @Override
    public DDLStatement create(DialectVisitor dialect) {
        return dialect.create(this);
    }

    public Table getParentTable() {
        return this.parentTable;
    }

    public void setParentTable(Table parentTable) {
        this.parentTable = parentTable;
        this.options.setParentColumn(this);
    }
}
