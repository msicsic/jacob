package sk.jacob.sql.ddl;

import sk.jacob.sql.dialect.DialectVisitor;

public interface DDLEpression {
    DDLStatement create(DialectVisitor visitor);
    DDLStatement drop(DialectVisitor visitor);
}
