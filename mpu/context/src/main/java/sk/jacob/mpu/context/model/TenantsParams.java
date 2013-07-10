package sk.jacob.mpu.context.model;

import sk.jacob.sql.Metadata;
import sk.jacob.sql.ddl.Column;
import static sk.jacob.sql.ddl.Column.options;
import static sk.jacob.sql.ddl.TYPE.String;
import sk.jacob.sql.ddl.Table;

public class TenantsParams extends Table {
    public static final String NAME = "TENANTS_PARAMS";
    /**
     * The foreign key to id of tenant.
     */
    public final Column tenantFk = new Column(this, "tenant_fk",
            String(150), options().foreignKey("tenants.id").nullable(false));
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

    public TenantsParams(Metadata metadata) {
        super(NAME, metadata);
    }
}
