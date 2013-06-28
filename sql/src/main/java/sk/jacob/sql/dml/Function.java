package sk.jacob.sql.dml;

import sk.jacob.sql.dialect.DialectVisitor;

public class Function {
    public static class Count implements ColumnPredicate {
        public final String columnName;
        public Count(String columnName) {
            this.columnName = columnName;
        }

        @Override
        public String sql(DialectVisitor visitor) {
            return visitor.visit(this);
        }
    }
    public static Count count(String columnName) {
        return new Count(columnName);
    }
}
