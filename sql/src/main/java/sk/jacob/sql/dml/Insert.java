package sk.jacob.sql.dml;

import sk.jacob.sql.ddl.ColumnValue;
import sk.jacob.sql.ddl.Table;
import sk.jacob.sql.dialect.DialectVisitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Insert extends DMLClause implements InsertClause {
    public final Table table;
    public final String tableName;
    private final List<ColumnValue> columnValues = new ArrayList<>();

    public Insert(String tableName) {
        this.tableName = tableName;
        this.table = null;
    }

    public Insert(Table table) {
        this.tableName = table.name;
        this.table = table;
    }

    @Override
    public SqlClause values(ColumnValue... columnValues) {
        this.columnValues.addAll(Arrays.asList(columnValues));
        return this;
    }

    @Override
    public SqlClause addValue(ColumnValue columnValue) {
        this.columnValues.add(columnValue);
        return this;
    }

    public List<ColumnValue> getColumnValues() {
        return this.columnValues;
    }

    @Override
    public String sql(DialectVisitor visitor) {
        return  visitor.sql(this);
    }
}
