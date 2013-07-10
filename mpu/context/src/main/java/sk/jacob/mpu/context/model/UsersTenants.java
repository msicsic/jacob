package sk.jacob.mpu.context.model;

import sk.jacob.sql.Metadata;
import sk.jacob.sql.ddl.Column;
import static sk.jacob.sql.ddl.Column.options;
import static sk.jacob.sql.ddl.TYPE.String;
import sk.jacob.sql.ddl.Table;

/**
 * Join table of tenants and users.
 */
public class UsersTenants extends Table {
    public static final String NAME = "USERS_TENANTS";
    /**
     * The login of user.
     */
    public final Column login = new Column(this, "login",
            String(50), options().nullable(false));
    /**
     * The foreign key to id of tenant.
     */
    public final Column tenantFk = new Column(this, "tenant_fk",
            String(150), options().foreignKey("tenants.id").nullable(false));

    public UsersTenants(Metadata metadata) {
        super(NAME, metadata);
    }
}
