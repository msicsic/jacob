package sk.jacob.mpu.security.dbregistry;

import sk.jacob.sql.Metadata;
import sk.jacob.sql.SequenceIdGenerator;

import static sk.jacob.sql.Column.options;
import static sk.jacob.sql.DDL.column;
import static sk.jacob.sql.DDL.table;
import static sk.jacob.sql.TYPE.Boolean;
import static sk.jacob.sql.TYPE.String;

public class Model {
    public static Metadata get() {
        return get(new Metadata());
    }

    public static Metadata get(Metadata metadata) {
        table("users", metadata,
                column("login", String().length(255), options().primaryKey()),
                column("username", String().length(255), options().nullable()),
                column("md5pwd", String().length(255), options().nullable()),
                column("token", String().length(255), options().nullable().unique()),
                column("admin", Boolean(), options().nullable()));
        return metadata;
    }
}
