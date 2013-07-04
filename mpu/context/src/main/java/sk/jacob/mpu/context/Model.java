package sk.jacob.mpu.context;

import sk.jacob.sql.Metadata;
import static sk.jacob.sql.ddl.Column.options;
import static sk.jacob.sql.ddl.DDL.column;
import static sk.jacob.sql.ddl.DDL.table;
import static sk.jacob.sql.ddl.TYPE.*;

public class Model {
    public static Metadata get() {
        return get(new Metadata());
    }

    public static Metadata get(Metadata metadata) {
        table("tenants", metadata,
                column("id", String(150), options().primaryKey()),
                column("name", String(150), options().nullable(false)));

        table("users_tenants", metadata,
                column("login", String(50), options().nullable(false)),
                column("tenant_fk", String(150), options().foreignKey("tenants.id").nullable(false)));

        table("tenants_params", metadata,
                column("tenant_fk", String(150), options().foreignKey("tenants.id").nullable(false)),
                column("param_name", String(100), options().nullable(false)),
                column("param_value", String(255), options().nullable(false)),
                column("scope", String(7), options().nullable(false)));

        table("ds", metadata,
                column("id", Long(), options().primaryKey()),
                column("url", String(150), options().nullable(false)));

        return metadata;
    }
}
