package sk.jacob.mpu.context.model;

import sk.jacob.sql.Metadata;
import sk.jacob.sql.ddl.Column;
import static sk.jacob.sql.ddl.Column.options;
import static sk.jacob.sql.ddl.TYPE.Long;
import static sk.jacob.sql.ddl.TYPE.String;
import static sk.jacob.sql.generator.SequenceIdGenerator.sequenceIdGenerator;

import sk.jacob.sql.ddl.Sequence;
import sk.jacob.sql.ddl.Table;

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

    //NOTE: Username and password can be part of enother entity - Authentication alias.
    /**
     * Username
     */
    public final Column username = new Column(this, "username",
            String(100), options().nullable(false));

    //TODO: Implement password encryption.
    /**
     * Password
     */
    public final Column password = new Column(this, "password",
            String(50), options().nullable(false));
}
