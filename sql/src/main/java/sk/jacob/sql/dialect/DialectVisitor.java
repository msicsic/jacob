package sk.jacob.sql.dialect;

import sk.jacob.sql.*;

public interface DialectVisitor {
    String visit(Select select);
    String visit(From from);
    DDLStatement visit(Table table);
    DDLStatement visit(Column column);
    String visit(Op.And and);
    String visit(Op.Eq eq);
    String visit(Op.Le le);

    String visit(TYPE.StringType stringType);

    String visit(Insert insert);
}
