package sk.jacob.mpu.context.model;

import sk.jacob.sql.Metadata;
import sk.jacob.sql.ddl.Column;
import sk.jacob.sql.ddl.Sequence;
import sk.jacob.sql.ddl.Table;

import static sk.jacob.sql.ddl.Column.options;
import static sk.jacob.sql.ddl.TYPE.Long;
import static sk.jacob.sql.ddl.TYPE.String;
import static sk.jacob.sql.generator.SequenceIdGenerator.sequenceIdGenerator;

/**
 * Datasources
 */
public class Ds extends Table {
    public static final String NAME = "DS";

    public Ds(Metadata metadata) {
        super(NAME, metadata);
        Sequence seq = metadata.sequence("DS_ID_SEQ");
        id = new Column(this, "id", Long(), options().primaryKey(sequenceIdGenerator(seq)));
    }

    /**
     * ID
     */
    public final Column id;

    /**
     * URL
     */
    public final Column url = new Column(this, "url",
            String(150), options().nullable(false));
}
