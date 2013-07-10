package sk.jacob.sql.Model;

import sk.jacob.sql.Metadata;

import static sk.jacob.sql.ddl.Column.options;
import static sk.jacob.sql.ddl.DDL.column;
import static sk.jacob.sql.ddl.DDL.table;
import static sk.jacob.sql.ddl.TYPE.Boolean;
import static sk.jacob.sql.ddl.TYPE.String;

public class TestModel {
    public static final Metadata METADATA = update(new Metadata());

    private static Metadata update(Metadata metadata) {
        return plainObjects(typeObjects(metadata));
    }

    private static Metadata plainObjects(Metadata metadata) {
        table("TAB_NULLABLE", metadata,
              column("C_STRING_NULLABLE", String(255), options().nullable(true).unique(false)),
              column("C_STRING_NOT_NULLABLE", String(255), options().nullable(false).unique(false)));

        table("TAB_UNIQUE", metadata,
              column("C_STRING_UNIQUE", String(255), options().nullable(true).unique(true)),
              column("C_STRING_NOT_UNIQUE", String(255), options().nullable(true).unique(false)));

        return metadata;
    }

    private static Metadata typeObjects(Metadata metadata) {
        return metadata;
    }
}
