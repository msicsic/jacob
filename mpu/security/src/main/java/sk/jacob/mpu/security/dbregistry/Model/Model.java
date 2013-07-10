package sk.jacob.mpu.security.dbregistry.Model;

import sk.jacob.sql.Metadata;

import static sk.jacob.sql.ddl.Column.options;
import static sk.jacob.sql.ddl.DDL.column;
import static sk.jacob.sql.ddl.DDL.table;
import static sk.jacob.sql.ddl.TYPE.Boolean;
import static sk.jacob.sql.ddl.TYPE.String;

public class Model {
    public static Metadata get() {
        return get(new Metadata());
    }

    public static Metadata get(Metadata metadata) {
        new Users(metadata);
        return metadata;
    }
}
