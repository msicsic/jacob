package sk.jacob.sql.model.tables;

import sk.jacob.sql.Metadata;
import sk.jacob.sql.ddl.Column;
import sk.jacob.sql.ddl.Table;

import static sk.jacob.sql.ddl.Column.options;
import static sk.jacob.sql.ddl.TYPE.*;

public class TabTUnique extends Table {
    public static final String NAME = "TABT_UNIQUE";

    public TabTUnique(Metadata metadata1) {
        super(NAME, metadata1);
    }

    public final Column cStringUnique = new Column(this, "C_STRING_UNIQUE",
                                                   String(255), options().nullable(true).unique(true));
    public final Column cStringNotUnique = new Column(this, "C_STRING_NOT_UNIQUE",
                                                      String(255), options().nullable(true).unique(false));

}
