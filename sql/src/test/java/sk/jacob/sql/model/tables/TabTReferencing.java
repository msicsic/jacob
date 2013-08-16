package sk.jacob.sql.model.tables;

import sk.jacob.sql.Metadata;
import sk.jacob.sql.ddl.Column;
import sk.jacob.sql.ddl.Table;

import static sk.jacob.sql.ddl.Column.options;
import static sk.jacob.sql.ddl.TYPE.*;


public class TabTReferencing extends Table {
    private static final String NAME = "TABT_REFERENCING";

    public TabTReferencing(Metadata metadata1) {
        super(NAME, metadata1);
        TabTIdInsert idInsertTable = metadata1.table(TabTIdInsert.class);
        cFk = new Column(this, "C_FK", Long(),
                         options().foreignKey(idInsertTable.cId));
    }

    public final Column cFk;
}
