package sk.jacob.sql.model.tables;

import sk.jacob.sql.Metadata;
import sk.jacob.sql.ddl.Column;
import sk.jacob.sql.ddl.Sequence;
import sk.jacob.sql.ddl.Table;
import sk.jacob.sql.model.TestModel;

import static sk.jacob.sql.ddl.DDL.sequence;
import static sk.jacob.sql.ddl.TYPE.*;

import static sk.jacob.sql.ddl.Column.options;
import static sk.jacob.sql.generator.SequenceIdGenerator.sequenceIdGenerator;

public class TabTIdInsert extends Table {
    public static final String NAME = "TABT_ID_INSERT";

    public TabTIdInsert(Metadata metadata1) {
        super(NAME, metadata1);
    }

    private Sequence idSequence = sequence("SEQUENCE_ID", super.metadata);

    public final Column cId = new Column(this, "C_ID", Long(),
                                         options().primaryKey(sequenceIdGenerator(this.idSequence)));
    public final Column cStringValue = new Column(this, "C_STRING_VALUE",
                                                  String(255), options().nullable(false).unique(false));
}
