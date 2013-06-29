package sk.jacob.sql.dml;

import sk.jacob.sql.dialect.DialectVisitor;

public interface SqlExpression {
    String sql(DialectVisitor visitor);
}
