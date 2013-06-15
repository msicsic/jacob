package sk.jacob.sql;
public class DDL {
    public static Table table(String name, Metadata metadata, Column... columns) {
        return new Table(name, metadata, columns);
    }
    public static Column column(String name, TYPE.Type type, Column.Options options) {
        return new Column(name, type, options);
    }
}
