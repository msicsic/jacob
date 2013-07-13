package sk.jacob.sql.ddl;

import sk.jacob.sql.dialect.DialectVisitor;

public class Column extends DbObject {
    public final TYPE.Type type;
    public final ColumnOptions options;
    public final String tableColumn;
    private Table parentTable;

    public Column(String tableColumn) {
        super(null);
        this.type = null;
        this.options = null;
        this.tableColumn = tableColumn;
    }

    public Column(String name, TYPE.Type type) {
        this(name, type, new ColumnOptions());
    }

    public Column(String name, TYPE.Type type, IColumnOptions options) {
        super(name);
        this.type = type;
        this.options = (ColumnOptions)options;
        this.tableColumn = null;
    }

    public Column(Table parentTable, String name, TYPE.Type type) {
        this(parentTable, name, type, new ColumnOptions());
    }

    public Column(Table parentTable, String name, TYPE.Type type, IColumnOptions options ) {
        super(name);
        this.type = type;
        this.options = (ColumnOptions)options;
        this.setParentTable(parentTable);
        this.tableColumn = null;
        parentTable.columns.add(this);
    }

    public static IColumnOptions options() {
        return new ColumnOptions();
    }

    @Override
    public DDLStatement create(DialectVisitor dialect) {
        return dialect.create(this);
    }

    @Override
    public DDLStatement drop(DialectVisitor visitor) {
        throw new UnsupportedOperationException();
    }

    public Table getParentTable() {
        return this.parentTable;
    }

    public void setParentTable(Table parentTable) {
        this.parentTable = parentTable;
        this.options.setParentColumn(this);
    }

    public String qname() {
        StringBuffer sb = new StringBuffer(this.parentTable.name);
        sb.append("_");
        sb.append(this.name);
        return sb.toString();
    }

    @Override
    public String dump() {
        return this.create().inline;
    }
}
