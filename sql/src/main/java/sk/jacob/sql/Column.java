package sk.jacob.sql;

import sk.jacob.sql.dialect.CompiledStatementList;
import sk.jacob.sql.dialect.DialectVisitor;

public class Column implements DbObject {
    public final String name;
    public final Class type;

    Column(String name, Class type, int ... precision) {
        this.name = name;
        this.type = type;
    }

    @Override
    public CompiledStatementList sql(DialectVisitor dialect) {
        return dialect.visit(this);
    }
}
