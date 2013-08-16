package sk.jacob.sql.ddl;

import sk.jacob.sql.*;

public class DDL {
    public static Table table(String tableName, Metadata metadata, Column... columns) {
        if(metadata.isDefined(tableName)) {
            throw new RuntimeException(tableName + " already defined.");
        } else {
            return new Table(tableName, metadata, columns);
        }
    }

    public static Column column(String name, TYPE.Type type, IColumnOptions columnOptions) {
        return new Column(name, type, columnOptions);
    }

    public static Column column(String name, TYPE.Type type) {
        return new Column(name, type);
    }

    public static Column column(String tableColumn) {
        return new Column(tableColumn);
    }

    public static Sequence sequence(String sequenceName, Metadata metadata) {
        if(metadata.isDefined(sequenceName)) {
            throw new RuntimeException(sequenceName + " already defined.");
        } else {
            return new Sequence(sequenceName, metadata);
        }
    }
}
