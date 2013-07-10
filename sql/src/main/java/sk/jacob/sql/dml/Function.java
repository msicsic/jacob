package sk.jacob.sql.dml;

import sk.jacob.sql.dialect.DialectVisitor;
import sk.jacob.sql.dialect.GenericDialectVisitor;

public class Function {
    public static class Count implements SqlClause {
        public final String columnName;
        public Count(String columnName) {
            this.columnName = columnName;
        }

        @Override
        public String sql(DialectVisitor visitor) {
            return visitor.sql(this);
        }
    }

    public static Count count(String columnName) {
        return new Count(columnName);
    }
}
