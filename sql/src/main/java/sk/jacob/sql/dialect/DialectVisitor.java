package sk.jacob.sql.dialect;

import sk.jacob.sql.Column;
import sk.jacob.sql.From;
import sk.jacob.sql.Query;
import sk.jacob.sql.Table;

import java.util.List;

public interface DialectVisitor {
    CompiledStatementList visit(Query query);
    CompiledStatementList visit(From from);
    CompiledStatementList visit(Table table);
    CompiledStatementList visit(Column column);
}
