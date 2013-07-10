package sk.jacob.sql.ddl;

public class ColumnValue {
    public final String columnName;
    public final String tableName;
    public final Object value;

    public ColumnValue(String columnName, Object value) {
        this.columnName = columnName;
        this.value = value;
        this.tableName = null;
    }

    public ColumnValue(Column column, Object value) {
        this.tableName =column.getParentTable().name;
        this.columnName = column.name;
        this.value = value;
    }
}
