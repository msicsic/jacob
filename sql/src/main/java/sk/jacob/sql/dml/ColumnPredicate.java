package sk.jacob.sql.dml;

import sk.jacob.sql.dialect.DialectVisitor;

public interface ColumnPredicate {
    String sql(DialectVisitor visitor);
}
