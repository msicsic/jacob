package sk.jacob.sql.dml;

import sk.jacob.sql.ddl.Column;
import sk.jacob.sql.ddl.ColumnValue;
import sk.jacob.sql.ddl.Table;

public class DML {
    public static SelectClause select(Object ... columnExpressions) {
        return new Select(columnExpressions);
    }

    public static InsertClause insert(String tableName) {
        return new Insert(tableName);
    }

    public static InsertClause insert(Table table) {
        return new Insert(table);
    }

    public static UpdateClause update(String tableName) {
        return new Update(tableName);
    }

    public static UpdateClause update(Table table) {
        return new Update(table);
    }

    public static DeleteClause delete(String tableName) {
        return new Delete(tableName);
    }

    public static DeleteClause delete(Table table) {
        return new Delete(table);
    }

    public static ColumnValue cv(String columnName, Object value) {
        return new ColumnValue(columnName, value);
    }

    public static ColumnValue cv(Column column, Object value) {
        return new ColumnValue(column, value);
    }
}
