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
        /* Enum of usernames
         *
         * login - login name of user
         * username - meaningful name of user
         * md5pwd - md5 hashed password
         * token - token string used for request authenticating
         * admin - mark if user is admin. Only one admin is permitted.
         */
        table("users", metadata,
                column("login", String(50), options().primaryKey()),
                column("username", String(100), options().nullable(false)),
                column("md5pwd", String(32), options().nullable(false)),
                column("token", String(256), options().nullable(false).unique(true)),
                column("admin", Boolean(), options().nullable(false)));
        return metadata;
    }
}
