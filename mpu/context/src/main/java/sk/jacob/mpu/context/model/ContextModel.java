package sk.jacob.mpu.context.model;

import sk.jacob.sql.Metadata;
import sk.jacob.sql.ddl.Sequence;
import sk.jacob.sql.ddl.Table;
import sk.jacob.sql.engine.DbEngine;

public enum ContextModel {
    INSTANCE;

    public final Metadata METADATA = populate(new Metadata());

    private Metadata populate(Metadata metadata) {
        new Sequence("DS_ID_SEQ", metadata);
        new Ds(metadata);
        new Tenants(metadata);
        new TenantsParams(metadata);
        new UsersTenants(metadata);
        return metadata;
    }

    public <T extends Table> T table(Class<T> tableClass) {
        return METADATA.table(tableClass);
    }
}
