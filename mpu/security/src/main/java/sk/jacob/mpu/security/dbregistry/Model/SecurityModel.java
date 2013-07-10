package sk.jacob.mpu.security.dbregistry.Model;

import sk.jacob.sql.Metadata;
import sk.jacob.sql.engine.DbEngine;

import static sk.jacob.sql.ddl.Column.options;
import static sk.jacob.sql.ddl.DDL.column;
import static sk.jacob.sql.ddl.DDL.table;
import static sk.jacob.sql.ddl.TYPE.Boolean;
import static sk.jacob.sql.ddl.TYPE.String;

public class SecurityModel {
    private static final Metadata METADATA = new Metadata();
    public static Metadata metadata() {
        return update(METADATA);
    }

    public static Metadata update(Metadata metadata) {
        new Users(metadata);
        return metadata;
    }

    public static <T> T table(Class<T> name) {
        return METADATA.table(name);
    }

    public static void createAll(DbEngine dbEngine) {
        METADATA.createAll(dbEngine);
    }
}
