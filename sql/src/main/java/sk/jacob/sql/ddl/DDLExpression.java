package sk.jacob.sql.ddl;

import sk.jacob.sql.dialect.DialectVisitor;

public interface DDLExpression {
    DDLStatement create(DialectVisitor visitor);
    DDLStatement drop(DialectVisitor visitor);
}
