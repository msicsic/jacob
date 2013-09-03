package sk.jacob.sql.model;

import sk.jacob.sql.Metadata;
import sk.jacob.sql.ddl.Sequence;
import sk.jacob.sql.model.tables.TabTIdInsert;
import sk.jacob.sql.model.tables.TabTNullable;
import sk.jacob.sql.model.tables.TabTReferencing;
import sk.jacob.sql.model.tables.TabTUnique;

import static sk.jacob.sql.ddl.Column.options;
import static sk.jacob.sql.ddl.DDL.*;
import static sk.jacob.sql.ddl.TYPE.Long;
import static sk.jacob.sql.ddl.TYPE.String;
import static sk.jacob.sql.generator.SequenceIdGenerator.sequenceIdGenerator;

public class TestModel {
    public static final Metadata METADATA = new Metadata();

    static {
        sequence("SEQUENCE_ID", METADATA);
        typeObjects();
        plainObjects();
    }

    private static void typeObjects() {
        new TabTNullable(METADATA);
        new TabTUnique(METADATA);
        new TabTIdInsert(METADATA);
        new TabTReferencing(METADATA);
    }

    private static void plainObjects() {
        table("TABP_NULLABLE", METADATA,
              column("C_STRING_NULLABLE", String(255), options().nullable(true).unique(false)),
              column("C_STRING_NOT_NULLABLE", String(255), options().nullable(false).unique(false)));

        table("TABP_UNIQUE", METADATA,
              column("C_STRING_UNIQUE", String(255), options().nullable(true).unique(true)),
              column("C_STRING_NOT_UNIQUE", String(255), options().nullable(true).unique(false)));

        Sequence sequence_id = METADATA.sequence("SEQUENCE_ID");
        table("TABP_ID_INSERT", METADATA,
              column("C_ID", Long(), options().primaryKey(sequenceIdGenerator(sequence_id))),
              column("C_STRING_VALUE", String(255), options().nullable(false).unique(false)));

        table("TABP_REFERENCING", METADATA,
              column("C_FK", Long(), options().foreignKey("TABP_ID_INSERT.C_ID")));
    }
}
