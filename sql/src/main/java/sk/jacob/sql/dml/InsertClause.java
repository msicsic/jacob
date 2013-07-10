package sk.jacob.sql.dml;

import sk.jacob.sql.ddl.ColumnValue;

public interface InsertClause {
    SqlClause values(ColumnValue... columnValues);
    SqlClause addValue(ColumnValue columnValue);
}
