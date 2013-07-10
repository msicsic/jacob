package sk.jacob.sql.dml;

import sk.jacob.sql.ddl.ColumnValue;

public interface UpdateClause {
    Set set(ColumnValue... columnValues);
    Set set(Set set);
}
