package sk.jacob.mpu.context.model;

import sk.jacob.sql.Metadata;
import sk.jacob.sql.ddl.Column;
import sk.jacob.sql.ddl.Table;

import static sk.jacob.sql.ddl.Column.options;
import static sk.jacob.sql.ddl.TYPE.String;

/**
 * Table of tenants.
 */
public class Tenants extends Table {
    public static final String NAME = "TENANTS";

    public Tenants(Metadata metadata) {
        super(NAME, metadata);
    }

    /**
     * The id of tenant.
     */
    public final Column id = new Column(this, "id",
                                        String(150), options().primaryKey());

    /**
     * The name of tenant.
     */
    public final Column name = new Column(this, "name",
                                          String(150), options().nullable(false));
}
