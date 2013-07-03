package sk.jacob.mpu.context;

import sk.jacob.sql.Metadata;

import static sk.jacob.sql.ddl.Column.options;
import static sk.jacob.sql.ddl.DDL.column;
import static sk.jacob.sql.ddl.DDL.table;
import static sk.jacob.sql.ddl.TYPE.String;

public class Model {
    public static Metadata get() {
        return get(new Metadata());
    }

    public static Metadata get(Metadata metadata) {
        /* Tenants enum
         *
         * name - tenant name
         */
        table("tenants", metadata,
                column("id", String(255), options().primaryKey()),
                column("name", String(255)));
        return metadata;
    }
}
