package sk.jacob.mpu.security.dbregistry;

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
        table("users", metadata,
                column("login", String(255), options().primaryKey()),
                column("username", String(255), options().nullable()),
                column("md5pwd", String(255), options().nullable()),
                column("token", String(255), options().nullable().unique()),
                column("admin", Boolean(), options().nullable()));
        return metadata;
    }
}
