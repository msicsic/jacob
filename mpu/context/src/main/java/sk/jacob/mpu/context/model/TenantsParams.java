package sk.jacob.mpu.context.model;

import sk.jacob.sql.Metadata;
import sk.jacob.sql.ddl.Column;
import sk.jacob.sql.ddl.Table;

import static sk.jacob.sql.ddl.Column.options;
import static sk.jacob.sql.ddl.TYPE.String;

public class TenantsParams extends Table {
    public static final String NAME = "TENANTS_PARAMS";

    public TenantsParams(Metadata metadata) {
        super(NAME, metadata);
        Tenants tenants = metadata.table(Tenants.class);
        tenantFk = new Column(this, "tenant_fk",
                              String(150), options().foreignKey(tenants.id).nullable(false));
    }

    /**
     * The foreign key to id of tenant.
     */
    public final Column tenantFk;

    /**
     * The name of parameter.
     */
    public final Column paramName = new Column(this, "param_name",
                                               String(100), options().nullable(false));

    /**
     * The value of parameter.
     */
    public final Column paramValue = new Column(this, "param_value",
                                                String(255), options().nullable(false));

    /**
     * The scope of parameter.
     */
    public final Column scope = new Column(this, "scope",
                                           String(7), options().nullable(false));

}
