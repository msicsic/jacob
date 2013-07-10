package sk.jacob.sql.dml;

import sk.jacob.sql.dialect.DialectVisitor;

public interface SqlClause {
    String sql(DialectVisitor visitor);
}
