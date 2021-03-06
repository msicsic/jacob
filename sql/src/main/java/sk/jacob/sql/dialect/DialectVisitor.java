package sk.jacob.sql.dialect;

import sk.jacob.sql.ddl.*;
import sk.jacob.sql.dml.*;

public interface DialectVisitor {
    String sql(Select select);
    String sql(From from);
    String sql(Op.And and);
    String sql(Op.Eq eq);
    String sql(Op.Le le);
    String sql(Op.In in);
    String sql(TYPE.StringType stringType);
    String sql(Insert insert);
    String sql(TYPE.BooleanType booleanType);
    String sql(Delete delete);
    String sql(Where where);
    String sql(TYPE.LongType longType);
    String sql(Function.Count count);
    String sql(Sequence sequence);
    String sql(Set set);
    String sql(Update update);
    String sql(LeftJoin leftJoin);
    String sql(InnerJoin innerJoin);
    DDLStatement create(Sequence sequence);
    DDLStatement create(Table table);
    DDLStatement create(Column column);
    DDLStatement create(ColumnOptions columnOptions);
    DDLStatement create(ForeignKey foreignKey);
    DDLStatement drop(Sequence sequence);
    DDLStatement drop(Table table);
}
