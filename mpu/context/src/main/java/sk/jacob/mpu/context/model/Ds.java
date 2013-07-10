package sk.jacob.mpu.context.model;

import sk.jacob.sql.Metadata;
import sk.jacob.sql.ddl.Column;
import static sk.jacob.sql.ddl.Column.options;
import static sk.jacob.sql.ddl.TYPE.Long;
import static sk.jacob.sql.ddl.TYPE.String;
import sk.jacob.sql.ddl.Table;

/**
 * Table of datastores.
 */
public class Ds extends Table {
    public static final String NAME = "DS";
    /**
     * The id of datastore.
     */
    public final Column id = new Column(this, "id",
            Long(), options().primaryKey());
    /**
     * The url of datastore.
     */
    public final Column url = new Column(this, "url",
            String(150), options().nullable(false));

    public Ds(Metadata metadata) {
        super(NAME, metadata);
    }
}
