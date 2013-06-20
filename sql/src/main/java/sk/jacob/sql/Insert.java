package sk.jacob.sql;

import sk.jacob.sql.dialect.DialectVisitor;

import java.util.Arrays;
import java.util.List;

public class Insert extends Statement {
    public final String tableName;
    public List<ColumnValue> columnValues;

    public Insert(String tableName) {
        this.tableName = tableName;
    }

    public Statement values(ColumnValue ... columnValues) {
        this.columnValues = Arrays.asList(columnValues);
        return this;
    }

    @Override
    public String sql(DialectVisitor visitor) {
        return  visitor.visit(this);
    }
}
