package sk.jacob.sql.dialect;

import sk.jacob.sql.Column;
import sk.jacob.sql.From;
import sk.jacob.sql.Query;
import sk.jacob.sql.Table;
import sk.jacob.util.func.Functional;
import sk.jacob.util.func.StringReducer;

import java.util.HashMap;
import java.util.Map;

public class GenericDialectVisitor implements DialectVisitor {
    private Map<Class, String> tm = typeMap();

    @Override
    public CompiledStatementList visit(Query query) {
        StringBuffer b = new StringBuffer("SELECT ");
        b.append((String) Functional.reduce(StringReducer.instance(", "), query.columnNames, null));
        return new CompiledStatementList(b);
    }

    @Override
    public CompiledStatementList visit(From from) {
        StringBuffer b = new StringBuffer("FROM ");
        b.append((String) Functional.reduce(StringReducer.instance(", "), from.tableNames, null));
        return new CompiledStatementList(b);
    }

    @Override
    public CompiledStatementList visit(Table table) {
        String sql = "CREATE TABLE " + table.name + " (\n";
        for(Column column : table.columns ) {
            sql += column.sql(this).toString() + ", ";
        }
        sql = sql.substring(0, sql.length()-2);
        sql += ");";
        return new CompiledStatementList(sql);
    }

    @Override
    public CompiledStatementList visit(Column column) {
        String sql = column.name;
        sql += " ";
        sql += tm.get(column.type);
        return new CompiledStatementList(sql);
    }

    private Map<Class, String> typeMap() {
        Map<Class, String> tm = new HashMap<Class, String>();
        tm.put(Integer.class, "INT");
        tm.put(String.class, "VARCHAR");
        return tm;
    }
}
