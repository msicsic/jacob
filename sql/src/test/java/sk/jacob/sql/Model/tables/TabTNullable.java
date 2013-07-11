package sk.jacob.sql.model.tables;

import sk.jacob.sql.Metadata;
import sk.jacob.sql.ddl.Column;
import sk.jacob.sql.ddl.Table;
import sk.jacob.sql.model.TestModel;

import static sk.jacob.sql.ddl.Column.options;
import static sk.jacob.sql.ddl.TYPE.*;

public class TabTNullable extends Table {
    public static final String NAME = "TABT_NULLABLE";

    public TabTNullable(Metadata metadata1) {
        super(NAME, metadata1);
    }

    public final Column cStringNullable = new Column(this, "C_STRING_NULLABLE",
                                                     String(255), options().nullable(true).unique(false));
    public final Column cStringNotNullable = new Column(this, "C_STRING_NOT_NULLABLE",
                                                        String(255), options().nullable(false).unique(false));

}
