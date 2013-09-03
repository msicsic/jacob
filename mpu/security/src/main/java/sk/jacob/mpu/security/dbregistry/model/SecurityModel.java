package sk.jacob.mpu.security.dbregistry.model;

import sk.jacob.sql.Metadata;
import sk.jacob.sql.ddl.Table;
import sk.jacob.sql.engine.DbEngine;

public enum SecurityModel {
    INSTANCE;

    public final Metadata METADATA = populate(new Metadata());

    private Metadata populate(Metadata metadata) {
        new Users(metadata);
        return metadata;
    }

    public <T extends Table> T table(Class<T> tableClass) {
        return METADATA.table(tableClass);
    }

    public void createAll(DbEngine dbEngine) {
        METADATA.createAll(dbEngine);
    }
}
