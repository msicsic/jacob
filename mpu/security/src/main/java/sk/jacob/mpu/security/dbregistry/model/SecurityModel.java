package sk.jacob.mpu.security.dbregistry.model;

import sk.jacob.sql.Metadata;
import sk.jacob.sql.engine.DbEngine;

import static sk.jacob.sql.ddl.DDL.column;

public enum SecurityModel {
    INSTANCE;

    public final Metadata METADATA = populate(new Metadata());

    private Metadata populate(Metadata metadata) {
        new Users(metadata);
        return metadata;
    }

    public <T> T table(Class<T> name) {
        return METADATA.table(name);
    }

    public void createAll(DbEngine dbEngine) {
        METADATA.createAll(dbEngine);
    }
}
