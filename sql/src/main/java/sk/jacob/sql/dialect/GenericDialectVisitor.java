package sk.jacob.sql.dialect;

import sk.jacob.sql.From;
import sk.jacob.sql.Query;
import sk.jacob.util.func.Functional;
import sk.jacob.util.func.StringReducer;

public class GenericDialectVisitor implements DialectVisitor {
    @Override
    public String visit(Query query) {
        StringBuffer b = new StringBuffer("SELECT ");
        b.append((String) Functional.reduce(StringReducer.instance(", "), query.columnNames, null));
        return b.toString();
    }

    @Override
    public String visit(From from) {
        StringBuffer b = new StringBuffer("FROM ");
        b.append((String) Functional.reduce(StringReducer.instance(", "), from.tableNames, null));
        return b.toString();
    }
}
