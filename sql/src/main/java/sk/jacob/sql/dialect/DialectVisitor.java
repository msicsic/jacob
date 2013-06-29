package sk.jacob.sql.dialect;

import sk.jacob.sql.ddl.*;
import sk.jacob.sql.dml.*;

public interface DialectVisitor {
    String visit(Select select);
    String visit(From from);
    String visit(Op.And and);
    String visit(Op.Eq eq);
    String visit(Op.Le le);
    String visit(TYPE.StringType stringType);
    String visit(Insert insert);
    String visit(TYPE.BooleanType booleanType);
    String visit(Delete delete);
    String visit(Where where);
    String visit(Column.Options options);
    String visit(TYPE.LongType longType);
    String visit(Function.Count count);
    DDLStatement create(Sequence sequence);
    DDLStatement create(Table table);
    DDLStatement create(Column column);
    String nextVal(Sequence sequence);
}
