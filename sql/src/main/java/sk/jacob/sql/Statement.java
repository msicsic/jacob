package sk.jacob.sql;

import sk.jacob.sql.dialect.DialectVisitor;

public interface Statement {
    String sql(DialectVisitor visitor);
}
