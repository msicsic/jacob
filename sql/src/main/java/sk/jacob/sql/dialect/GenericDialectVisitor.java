package sk.jacob.sql.dialect;

import sk.jacob.sql.*;
import sk.jacob.util.func.Functional;
import sk.jacob.util.func.StringReducer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenericDialectVisitor implements DialectVisitor {
    private Map<Class, String> tm = typeMap();

    @Override
    public String visit(Select select) {
        StringBuffer sb = new StringBuffer("SELECT ");
        sb.append((String) Functional.reduce(StringReducer.instance(", "), select.columnNames, null));
        return sb.toString();
    }

    @Override
    public String visit(From from) {
        StringBuffer sb = new StringBuffer("FROM ");
        sb.append((String) Functional.reduce(StringReducer.instance(", "), from.tableNames, null));
        return sb.toString();
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

    @Override
    public String visit(Op.And and) {
        StringBuffer sb = new StringBuffer("(");
        List<String> coSql = new ArrayList<String>(and.conditionalOperations.size());
        for (ConditionalOperation co : and.conditionalOperations) {
            co.paramCounter = and.paramCounter;
            coSql.add(co.sql(this));
        }
        String andStatement = (String)Functional.reduce(StringReducer.instance(" AND "), coSql, null );
        sb.append(andStatement);
        sb.append(")");
        return sb.toString();
    }

    @Override
    public String visit(Op.Eq eq) {
        StringBuffer sb = new StringBuffer(eq.columnName);
        sb.append(" = ");
        sb.append(eq.paramCounter.addParam(eq.columnName, eq.value));
        return sb.toString();
    }

    @Override
    public String visit(Op.Le le) {
        StringBuffer sb = new StringBuffer(le.columnName);
        sb.append(" < ");
        sb.append(le.paramCounter.addParam(le.columnName, le.value));
        return sb.toString();
    }

    private Map<Class, String> typeMap() {
        Map<Class, String> tm = new HashMap<Class, String>();
        tm.put(Integer.class, "INT");
        tm.put(String.class, "VARCHAR");
        return tm;
    }
}
