package sk.jacob.sql.dialect;

import sk.jacob.sql.ddl.*;
import sk.jacob.sql.dml.*;
import sk.jacob.common.util.func.Functional;
import sk.jacob.common.util.func.StringReducer;

import java.util.ArrayList;
import java.util.List;

public class GenericDialectVisitor implements DialectVisitor {
    public static GenericDialectVisitor INSTANCE = new GenericDialectVisitor();
    protected GenericDialectVisitor(){}

    @Override
    public String sql(Select select) {
        StringBuffer sb = new StringBuffer("SELECT ");
        sb.append((String) Functional.reduce(StringReducer.instance(", "),
                                             normalizeColumnExpressions(select.columnExpressions)));
        sb.append("\n");
        From fromClause = select.getFromClause();
        if(fromClause != null) {
            String fromSql = fromClause.sql(this);
            sb.append(fromSql);
        }
        sb.append(";");
        return sb.toString();
    }

    protected List<String> normalizeColumnExpressions(List columnExpressions) {
        List<String> normalizedExpressions = new ArrayList<>();
        for(Object expression : columnExpressions) {
            normalizedExpressions.add(normalizeColumnExpression(expression));
        }
        return normalizedExpressions;
    }

    protected String normalizeColumnExpression(Object columnExpression) {
        String normalizedCE = null;
        if (columnExpression instanceof String) {
            normalizedCE = (String)columnExpression;
        } else if(columnExpression instanceof SqlClause) {
            normalizedCE = ((SqlClause)columnExpression).sql(this);
        } else if(columnExpression instanceof Column) {
            Column c = (Column)columnExpression;
            StringBuffer sb = new StringBuffer(c.qname());
            sb.append(" AS ");
            sb.append(c.alias());
            normalizedCE = sb.toString();
        }
        return normalizedCE;
    }

    @Override
    public String sql(Insert insert) {
        StringBuffer sb = new StringBuffer("INSERT INTO ");
        sb.append(insert.tableName);
        sb.append(" (");
        for(ColumnValue cv : insert.getColumnValues()) {
            sb.append(cv.columnName);
            sb.append(", ");
        }
        sb.setLength(sb.length() - 2);
        sb.append(")\nVALUES (");

        for(ColumnValue cv : insert.getColumnValues()) {
            sb.append(insert.getParamCounter().addParam(cv.columnName, cv.value));
            sb.append(", ");
        }
        sb.setLength(sb.length() - 1);
        sb.append(")");
        return sb.toString();
    }

    @Override
    public String sql(Delete delete) {
        StringBuffer sb = new StringBuffer("DELETE FROM ");
        sb.append(delete.tableName);
        sb.append(" ");
        sb.append(delete.getWhereClause().sql(this));
        return sb.toString();
    }

    @Override
    public String sql(From from) {
        StringBuffer sb = new StringBuffer("FROM ");
        List<String> normalizeTables = normalizeTableExpressions(from.tableExpressions);
        sb.append((String) Functional.reduce(StringReducer.instance(", "), normalizeTables));

        Where whereClause = from.getWhereClause();
        if( from.joins.size() != 0 ) {
            for(Join join : from.joins) {
                sb.append("\n");
                sb.append(join.sql(this));
                sb.append(" ");
            }
        }
        if( whereClause != null ) {
            sb.append("\n");
            String whereSql = whereClause.sql(this);
            sb.append(whereSql);
        }
        return sb.toString();
    }

    @Override
    public String sql(LeftJoin leftJoin) {
        StringBuffer sb = new StringBuffer("LEFT OUTER JOIN ");
        sb.append(normalizeTableExpression(leftJoin.tableExpression));
        sb.append(" ON ");
        sb.append(leftJoin.conditionalOperation.sql(this));
        return sb.toString();
    }

    @Override
    public String sql(InnerJoin innerJoin) {
        StringBuffer sb = new StringBuffer("JOIN ");
        sb.append(normalizeTableExpression(innerJoin.tableExpression));
        sb.append(" ON ");
        sb.append(innerJoin.conditionalOperation.sql(this));
        return sb.toString();
    }

    protected List<String> normalizeTableExpressions(List tableExpressions) {
        List<String> normalizedPredicates = new ArrayList<>();
        for(Object expression : tableExpressions) {
            normalizedPredicates.add(normalizeTableExpression(expression));
        }
        return normalizedPredicates;
    }

    protected String normalizeTableExpression(Object tableExpression) {
        String normalizedTE = null;
        if (tableExpression instanceof String) {
            normalizedTE = (String)tableExpression;
        } else if(tableExpression instanceof Table) {
            normalizedTE = ((Table)tableExpression).name;
        }
        return normalizedTE;
    }

    @Override
    public String sql(Where where) {
        StringBuffer sb = new StringBuffer("WHERE ");
        String coSql = where.conditionalOperation.sql(this);
        sb.append(coSql);
        return sb.toString();
    }

    @Override
    public String sql(TYPE.StringType stringType) {
        StringBuffer sb = new StringBuffer("VARCHAR(");
        sb.append(stringType.length);
        sb.append(")");
        return sb.toString();
    }

    @Override
    public String sql(TYPE.BooleanType booleanType) {
        return "BOOLEAN";
    }

    @Override
    public String sql(TYPE.LongType longType) {
        return "NUMBER";
    }

    @Override
    public String sql(Function.Count count) {
        StringBuffer sb = new StringBuffer("COUNT(");
        sb.append(count.columnName);
        sb.append(")");
        return sb.toString();
    }

    @Override
    public String sql(Sequence sequence) {
        StringBuffer sb = new StringBuffer("SELECT ");
        sb.append(sequence.name);
        sb.append(".NEXTVAL AS ID FROM DUAL;");
        return sb.toString();
    }

    @Override
    public String sql(Set set) {
        StringBuffer sb = new StringBuffer("SET ");
        for(ColumnValue cv : set.getColumnValues()) {
            sb.append(cv.columnName);
            sb.append("=");
            sb.append(set.getParamCounter().addParam(cv.columnName, cv.value));
            sb.append(", ");
        }
        int lastComma = sb.lastIndexOf(",");
        sb.replace(lastComma, lastComma + 2, "\n");
        sb.append(set.getWhereClause().sql(this));
        return sb.toString();
    }

    @Override
    public String sql(Update update) {
        StringBuffer sb = new StringBuffer("UPDATE ");
        sb.append(update.tableName);
        sb.append("\n");
        sb.append(update.getSetStatement().sql(this));
        return sb.toString();
    }

    @Override
    public String sql(Op.And and) {
        StringBuffer sb = new StringBuffer("(");
        List<String> coSql = new ArrayList<>(and.conditionalOperations.size());
        for (ConditionalOperation co : and.conditionalOperations) {
            coSql.add(co.sql(this));
        }
        String andStatement = (String)Functional.reduce(StringReducer.instance(" AND "), coSql);
        sb.append(andStatement);
        sb.append(")");
        return sb.toString();
    }

    @Override
    public String sql(Op.Eq eq) {
        return  binaryColumnOperation(eq.column, eq.value, "=", eq.getParamCounter());
    }

    @Override
    public String sql(Op.Le le) {
        return  binaryColumnOperation(le.column, le.value, "<", le.getParamCounter());
    }

    @Override
    public String sql(Op.In in) {
        String wc = whereColumn(in.column);
        StringBuilder sb = new StringBuilder(wc);
        sb.append(" IN (");
        for(Object value : in.values) {
            sb.append(in.getParamCounter().addParam(wc, value));
            sb.append(", ");
        }
        sb.setLength(sb.length() - 2);
        sb.append(")");
        return sb.toString();
    }

    protected String binaryColumnOperation(Object column, Object value, String sign, DMLClause.ParamCounter pc) {
        String wc = whereColumn(column);
        StringBuffer sb = new StringBuffer(wc);
        sb.append(" ");
        sb.append(sign);
        sb.append(" ");

        if (value instanceof Column) {
            sb.append(columnValue((Column) value));
        } else {
            sb.append(pc.addParam(wc, value));
        }

        return sb.toString();
    }

    protected String whereColumn(Object columnExpression) {
        String normalizedCE = null;
        if (columnExpression instanceof String) {
            normalizedCE = (String)columnExpression;
        } else if(columnExpression instanceof Column) {
            normalizedCE = columnValue((Column) columnExpression);
        }
        return normalizedCE;
    }

    protected String columnValue(Column column) {
        StringBuffer sb = new StringBuffer();
        if (column.name == null) {
            sb.append(column.tableColumn);
        } else {
            sb.append(column.getParentTable().name);
            sb.append(".");
            sb.append(column.name);
        }
        return sb.toString();
    }

    @Override
    public DDLStatement create(Table table) {
        StringBuffer sb = new StringBuffer("CREATE TABLE IF NOT EXISTS ");
        sb.append(table.name);
        sb.append(" (\n");
        List<String> columnInlineStatements = new ArrayList<>();
        List<String> outlineStatements = new ArrayList<>();
        for(Column column : table.columns ) {
            DDLStatement csl = column.create(this);
            columnInlineStatements.add(csl.inline);
            outlineStatements.addAll(csl.outline);
        }
        sb.append(Functional.reduce(StringReducer.instance(",\n"), columnInlineStatements));
        sb.append(");");
        return new DDLStatement(sb.toString(), outlineStatements);
    }

    @Override
    public DDLStatement create(Column column) {
        StringBuffer sb = new StringBuffer(column.name);
        sb.append(" ");
        sb.append(column.type.sql(this));
        sb.append(" ");
        ColumnOptions columnOptions = column.options;
        DDLStatement optionsStatement =  columnOptions.create(this);
        sb.append(optionsStatement.inline);
        return new DDLStatement(sb.toString(), optionsStatement.outline);
    }

    @Override
    public DDLStatement create(ColumnOptions columnOptions) {
        StringBuffer inlineSb = new StringBuffer();
        List<String> outline = new ArrayList<>();

        if(columnOptions.isPrimaryKey() == true) {
            inlineSb.append(" PRIMARY KEY ");
        }

        if(   columnOptions.isNullable() == false
           && columnOptions.isPrimaryKey() == false) {
            inlineSb.append(" NOT NULL ");
        }

        if(   columnOptions.isUnique() == true
           && columnOptions.isPrimaryKey() == false) {
            inlineSb.append(" UNIQUE ");
        }

        ForeignKey foreignKey = columnOptions.getForeignKey();
        if (foreignKey != null) {
            DDLStatement fkDdl = foreignKey.create(this);
            outline.addAll(fkDdl.outline);
        }

        return new DDLStatement(inlineSb.toString(), outline);
    }

    @Override
    public DDLStatement create(ForeignKey foreignKey) {
        StringBuffer sb = new StringBuffer("ALTER TABLE ");
        sb.append(foreignKey.getParentColumn().getParentTable().name);
        sb.append(" ADD FOREIGN KEY (");
        sb.append(foreignKey.getParentColumn().name);
        sb.append(") REFERENCES ");
        sb.append(foreignKey.refTableName);
        sb.append("(");
        sb.append(foreignKey.refColumnName);
        sb.append(");");
        List<String> outline = new ArrayList<>();
        outline.add(sb.toString());
        return new DDLStatement(null, outline);
    }

    @Override
    public DDLStatement create(Sequence sequence) {
        StringBuffer sb = new StringBuffer("CREATE SEQUENCE IF NOT EXISTS ");
        sb.append(sequence.name);
        sb.append(";");
        return new DDLStatement(sb.toString());
    }

    @Override
    public DDLStatement drop(Sequence sequence) {
        StringBuffer sb = new StringBuffer("DROP SEQUENCE IF EXISTS ");
        sb.append(sequence.name);
        sb.append(";");
        return new DDLStatement(sb.toString());
    }

    @Override
    public DDLStatement drop(Table table) {
        StringBuffer sb = new StringBuffer("DROP TABLE IF EXISTS ");
        sb.append(table.name);
        sb.append(";");
        return new DDLStatement(sb.toString());
    }
}
