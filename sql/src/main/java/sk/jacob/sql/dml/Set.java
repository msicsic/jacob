package sk.jacob.sql.dml;

import sk.jacob.sql.ddl.ColumnValue;
import sk.jacob.sql.dialect.DialectVisitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Set extends DMLStatement {
    private final List<ColumnValue> columnValues = new ArrayList<ColumnValue>();
    private Where where;

    public Set(DMLStatement parentDMLStatement, ColumnValue... columnValues) {
        super(parentDMLStatement);
        this.columnValues.addAll(Arrays.asList(columnValues));
    }

    public Where where(ConditionalOperation conditionalOperation) {
        return where( new Where(this, conditionalOperation) );
    }

    public Where where(Where where) {
        this.where = where;
        return this.where;
    }

    public Where getWhereClause() {
        return this.where;
    }

    public List<ColumnValue> getColumnValues() {
        return this.columnValues;
    }

    @Override
    public String sql(DialectVisitor visitor) {
        return visitor.sql(this);
    }
}
