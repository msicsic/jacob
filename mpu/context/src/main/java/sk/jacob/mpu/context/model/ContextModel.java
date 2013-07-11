package sk.jacob.mpu.context.model;

import sk.jacob.sql.Metadata;
import sk.jacob.sql.engine.DbEngine;

public class ContextModel {
    private static final Metadata METADATA = new Metadata();

    public static Metadata metadata() {
        return update(METADATA);
    }

    public static Metadata update(Metadata metadata) {
        new Ds(metadata);
        new Tenants(metadata);
        new TenantsParams(metadata);
        new UsersTenants(metadata);
        return metadata;
    }

    public static <T> T table(Class<T> name) {
        return METADATA.table(name);
    }
}
