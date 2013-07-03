package sk.jacob.mpu.context.tenant;

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
        //TODO foreign keys
        table("tenants", metadata,
                column("id", String(255), options().primaryKey()),
                column("name", String(255), options()));
        
        table("tenants_params", metadata,
                column("tenant_fk", String(255), options()),
                column("param_name", String(255), options()),
                column("param_value", String(255), options()),
                column("scope", String(255), options()));
        
        table("users_tenants", metadata,
                column("login", String(255), options()),
                column("tenant_fk", String(255), options()));
        
        //TODO ds table
        
        return metadata;
    }
}
