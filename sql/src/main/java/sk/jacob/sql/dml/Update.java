package sk.jacob.sql.dml;

import sk.jacob.sql.ddl.ColumnValue;
import sk.jacob.sql.dialect.DialectVisitor;

public class Update extends DMLStatement {
    public final String tableName;
    private Set set;

    public Update(String tableName) {
        this.tableName = tableName;
    }

    public Set set(ColumnValue... columnValues) {
        return set(new Set(this, columnValues));
    }

    public Set set(Set set) {
        this.set = set;
        return this.set;
    }

    public Set getSetStatement() {
        return this.set;
    }

    @Override
    public String sql(DialectVisitor visitor) {
        return visitor.visit(this);
    }
}
