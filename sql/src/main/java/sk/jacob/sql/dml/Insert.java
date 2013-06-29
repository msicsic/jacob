package sk.jacob.sql.dml;

import sk.jacob.sql.ddl.ColumnValue;
import sk.jacob.sql.ddl.Table;
import sk.jacob.sql.dialect.DialectVisitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Insert extends DMLStatement {
    public final String tableName;
    public final Table table;
    private final List<ColumnValue> columnValues = new ArrayList<ColumnValue>();

    public Insert(String tableName) {
        this.tableName = tableName;
        this.table = null;
    }

    public Insert(Table table) {
        this.tableName = table.name;
        this.table = table;
    }

    public DMLStatement values(ColumnValue ... columnValues) {
        this.columnValues.addAll(Arrays.asList(columnValues));
        return this;
    }

    public DMLStatement addValue(ColumnValue columnValue) {
        this.columnValues.add(columnValue);
        return this;
    }

    public List<ColumnValue> getColumnValues() {
        return this.columnValues;
    }

    @Override
    public String sql(DialectVisitor visitor) {
        return  visitor.visit(this);
    }
}
