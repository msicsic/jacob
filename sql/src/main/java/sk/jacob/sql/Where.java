package sk.jacob.sql;

import sk.jacob.sql.dialect.DialectVisitor;

public class Where implements Statement {
    private From from;

    public Where(From from) {
        this.from = from;
    }

    @Override
    public String sql(DialectVisitor visitor) {
        return from.sql(visitor);
    }
}
