package sk.jacob.sql.ddl;

import sk.jacob.sql.dialect.DialectVisitor;

public class Column extends DbObject {
    public final TYPE.Type type;
    public final ColumnOptions options;
    private Table parentTable;

    Column(String name, TYPE.Type type) {
        this(name, type, new ColumnOptions());
    }

    Column(String name, TYPE.Type type, ColumnOptions options) {
        super(name);
        this.type = type;
        this.options = options;
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
