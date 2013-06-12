package sk.jacob.sql.dialect;

import sk.jacob.sql.*;

public interface DialectVisitor {
    String visit(Select select);
    String visit(From from);
    CompiledStatementList visit(Table table);
    CompiledStatementList visit(Column column);
    String visit(Op.And and);
    String visit(Op.Eq eq);
    String visit(Op.Le le);
}
