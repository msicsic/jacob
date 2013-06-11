package sk.jacob.sql;

import sk.jacob.sql.dialect.CompiledStatementList;
import sk.jacob.sql.dialect.DialectVisitor;

public interface Statement {
    CompiledStatementList sql(DialectVisitor visitor);
}
