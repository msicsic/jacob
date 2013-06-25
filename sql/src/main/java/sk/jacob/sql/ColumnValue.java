package sk.jacob.sql;

public class ColumnValue {
    public final String columnName;
    public final Object value;

    public ColumnValue(String columnName, Object value) {
        this.columnName = columnName;
        this.value = value;
    }
}