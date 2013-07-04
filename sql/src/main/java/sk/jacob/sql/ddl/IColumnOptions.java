package sk.jacob.sql.ddl;

import sk.jacob.sql.generator.IdGenerator;

public interface IColumnOptions {
    IColumnOptions primaryKey();
    IColumnOptions primaryKey(IdGenerator generator);
    IColumnOptions nullable(Boolean nullable);
    IColumnOptions unique(Boolean unique);
    IColumnOptions foreignKey(String refTabCol);
    IColumnOptions foreignKey(String refTabCol, String constraintName);
}
