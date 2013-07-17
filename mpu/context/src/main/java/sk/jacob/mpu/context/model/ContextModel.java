package sk.jacob.mpu.context.model;

import sk.jacob.sql.Metadata;
import sk.jacob.sql.engine.DbEngine;

public enum ContextModel {
    INSTANCE;

    public final Metadata METADATA = populate(new Metadata());

    private Metadata populate(Metadata metadata) {
        new Ds(metadata);
        new Tenants(metadata);
        new TenantsParams(metadata);
        new UsersTenants(metadata);
        return metadata;
    }

    public  <T> T table(Class<T> name) {
        return METADATA.table(name);
    }
}
