package sk.jacob.sql;

import sk.jacob.sql.dialect.CompiledStatementList;
import sk.jacob.sql.dialect.DialectVisitor;

public class Column extends DbObject {
    public final String name;
    public final Class type;

    Column(String name, Class type, int ... precision) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String sql(DialectVisitor dialect) {
        return null; //dialect.visit(this);
    }

    @Override
    public Statement rootStatement() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
