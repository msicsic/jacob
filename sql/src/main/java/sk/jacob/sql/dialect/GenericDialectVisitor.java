package sk.jacob.sql.dialect;

import sk.jacob.sql.*;
import sk.jacob.util.func.Functional;
import sk.jacob.util.func.StringReducer;

import java.util.ArrayList;
import java.util.List;

public class GenericDialectVisitor implements DialectVisitor {
    @Override
    public String visit(Select select) {
        StringBuffer sb = new StringBuffer("SELECT ");
        sb.append((String) Functional.reduce(StringReducer.instance(", "), select.columnNames));
        sb.append("\n");
        if(select.from != null) {
            String fromSql = select.from.sql(this);
            sb.append(fromSql);
        }
        sb.append(";");
        return sb.toString();
    }

    @Override
    public String visit(Insert insert) {
        StringBuffer sb = new StringBuffer("INSERT INTO ");
        sb.append(insert.tableName);
        sb.append(" (");
        for(ColumnValue cv : insert.columnValues) {
            sb.append(cv.columnName);
            sb.append(", ");
        }
        int lastComma = sb.lastIndexOf(",");
        sb.replace(lastComma, lastComma + 2, ")\nVALUES (");

        for(ColumnValue cv : insert.columnValues) {
            sb.append(insert.getParamCounter().addParam(cv.columnName, cv.value));
            sb.append(", ");
        }
        lastComma = sb.lastIndexOf(",");
        sb.replace(lastComma, lastComma + 1, ")");

        return sb.toString();
    }

    @Override
    public String visit(Delete delete) {
        StringBuffer sb = new StringBuffer("DELETE FROM ");
        sb.append(delete.tableName);
        sb.append(" ");
        sb.append(delete.where.sql(this));
        return sb.toString();
    }

    @Override
    public String visit(From from) {
        StringBuffer sb = new StringBuffer("FROM ");
        sb.append((String) Functional.reduce(StringReducer.instance(", "), from.tableNames));
        if(from.where != null) {
            sb.append("\n");
            String whereSql = from.where.sql(this);
            sb.append(whereSql);
        }
        return sb.toString();
    }

    @Override
    public String visit(Where where) {
        StringBuffer sb = new StringBuffer("WHERE ");
        String coSql = where.conditionalOperation.sql(this);
        sb.append(coSql);
        return sb.toString();
    }

    @Override
    public String visit(Column.Options options) {
        return options.primaryKey == Boolean.TRUE ? " PRIMARY KEY " : "";
    }

    @Override
    public DDLStatement visit(Table table) {
        StringBuffer sb = new StringBuffer("CREATE TABLE IF NOT EXISTS ");
        sb.append(table.name);
        sb.append(" (\n");
        List<String> columnInlineStatements = new ArrayList<String>();
        List<String> outlineStatements = new ArrayList<String>();
        for(Column column : table.columns ) {
            DDLStatement csl = column.sql(this);
            columnInlineStatements.add(csl.inline);
            outlineStatements.addAll(csl.outline);
        }
        sb.append(Functional.reduce(StringReducer.instance(", \n"), columnInlineStatements));
        sb.append(");");
        return new DDLStatement(sb.toString(), outlineStatements);
    }

    @Override
    public DDLStatement visit(Column column) {
        StringBuffer sb = new StringBuffer(column.name);
        sb.append(" ");
        if(column.type instanceof TYPE.StringType) {
            sb.append(this.visit((TYPE.StringType)column.type));
        } else if(column.type instanceof TYPE.BooleanType) {
            sb.append(this.visit((TYPE.BooleanType)column.type));
        }
        sb.append(" ");
        sb.append(visit(column.options));
        return new DDLStatement(sb.toString(), null);
    }

    @Override
    public String visit(TYPE.StringType stringType) {
        StringBuffer sb = new StringBuffer("VARCHAR(");
        sb.append(stringType.length);
        sb.append(")");
        return sb.toString();
    }

    @Override
    public String visit(TYPE.BooleanType booleanType) {
        return "BOOLEAN";
    }

    @Override
    public String visit(Op.And and) {
        StringBuffer sb = new StringBuffer("(");
        List<String> coSql = new ArrayList<String>(and.conditionalOperations.size());
        for (ConditionalOperation co : and.conditionalOperations) {
            coSql.add(co.sql(this));
        }
        String andStatement = (String)Functional.reduce(StringReducer.instance(" AND "), coSql);
        sb.append(andStatement);
        sb.append(")");
        return sb.toString();
    }

    @Override
    public String visit(Op.Eq eq) {
        StringBuffer sb = new StringBuffer(eq.columnName);
        sb.append(" = ");
        sb.append(eq.getParamCounter().addParam(eq.columnName, eq.value));
        return sb.toString();
    }

    @Override
    public String visit(Op.Le le) {
        StringBuffer sb = new StringBuffer(le.columnName);
        sb.append(" < ");
        sb.append(le.getParamCounter().addParam(le.columnName, le.value));
        return sb.toString();
    }

}
