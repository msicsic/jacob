package sk.jacob.sql.dml;

import sk.jacob.sql.ddl.ColumnValue;
import sk.jacob.sql.dialect.DialectVisitor;
import sk.jacob.sql.dialect.GenericDialectVisitor;

public class Update extends DMLClause implements UpdateClause {
    public final String tableName;
    private Set set;

    public Update(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public Set set(ColumnValue... columnValues) {
        return set(new Set(this, columnValues));
    }

    @Override
    public Set set(Set set) {
        this.set = set;
        return this.set;
    }

    public Set getSetStatement() {
        return this.set;
    }

    @Override
    public String sql(DialectVisitor visitor) {
        return visitor.sql(this);
    }
}
