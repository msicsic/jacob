package sk.jacob.sql.model.tables;

import sk.jacob.sql.Metadata;
import sk.jacob.sql.ddl.Column;
import sk.jacob.sql.ddl.Sequence;
import sk.jacob.sql.ddl.Table;

import static sk.jacob.sql.ddl.Column.options;
import static sk.jacob.sql.ddl.TYPE.Long;
import static sk.jacob.sql.ddl.TYPE.String;
import static sk.jacob.sql.generator.SequenceIdGenerator.sequenceIdGenerator;

public class TabTIdInsert extends Table {
    private static final String NAME = "TABT_ID_INSERT";

    public TabTIdInsert(Metadata metadata) {
        super(NAME, metadata);
        idSequence = metadata.sequence("SEQUENCE_ID");
        cId = new Column(this, "C_ID", Long(),
                         options().primaryKey(sequenceIdGenerator(this.idSequence)));
    }

    private Sequence idSequence;

    public final Column cId;

    public final Column cStringValue = new Column(this, "C_STRING_VALUE",
                                                  String(255), options().nullable(false).unique(false));
}
