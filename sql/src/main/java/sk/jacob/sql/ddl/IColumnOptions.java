package sk.jacob.sql.ddl;

import sk.jacob.sql.generator.IdGenerator;

public interface IColumnOptions {
    IColumnOptions primaryKey();
    IColumnOptions primaryKey(IdGenerator generator);
    IColumnOptions nullable(boolean nullable);
    IColumnOptions unique(boolean unique);
    IColumnOptions foreignKey(String refTabCol);
    IColumnOptions foreignKey(String refTabCol, String constraintName);
    IColumnOptions foreignKey(Column column);
    IColumnOptions foreignKey(Column column, String constraintName);
}
