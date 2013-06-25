package sk.jacob.mpu.security.dbregistry;

import sk.jacob.sql.Metadata;

import static sk.jacob.sql.DDL.*;
import static sk.jacob.sql.TYPE.*;
import static sk.jacob.sql.Column.options;

public class Model {
    public static Metadata get() {
        return get(new Metadata());
    }

    public static Metadata get(Metadata metadata) {
        table("users", metadata,
                column("login", String().length(255), options().primaryKey(true)),
                column("username", String().length(255), options().nullable(false)),
                column("md5pwd", String().length(255), options().nullable(false)),
                column("token", String().length(255), options().nullable(true).unique(true)),
                column("admin", Boolean(), options().nullable(true)));

        return metadata;
    }
}
