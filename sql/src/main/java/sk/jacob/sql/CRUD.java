package sk.jacob.sql;

public class CRUD {
    public static Select select(String ... columnNames) {
        return new Select(columnNames);
    }

    public static Insert insert(String tableName) {
        return new Insert(tableName);
    }

    public static Insert insert(Table table) {
        return new Insert(table);
    }

    public static Update update(String tableName) {
        return new Update(tableName);
    }

    public static Delete delete(String tableName) {
        return new Delete(tableName);
    }

    public static ColumnValue cv(String columnName, Object value) {
        return new ColumnValue(columnName, value);
    }
}
