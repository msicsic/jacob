package sk.jacob.sql.ddl;

import sk.jacob.sql.*;

public class DDL {
    public static Table table(String name, Metadata metadata, Column... columns) {
        return new Table(name, metadata, columns);
    }

    public static Column column(String name, TYPE.Type type, ColumnOptions columnOptions) {
        return new Column(name, type, columnOptions);
    }

    public static Column column(String name, TYPE.Type type) {
        return new Column(name, type);
    }

    public static Sequence sequence(String sequenceName, Metadata metadata) {
        return new Sequence(sequenceName, metadata);
    }
}
