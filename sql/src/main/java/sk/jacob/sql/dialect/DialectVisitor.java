package sk.jacob.sql.dialect;

import sk.jacob.sql.From;
import sk.jacob.sql.Query;

public interface DialectVisitor {
    String visit(Query query);
    String visit(From from);
}
