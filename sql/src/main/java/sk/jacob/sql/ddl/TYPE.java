package sk.jacob.sql.ddl;

import sk.jacob.sql.dialect.DialectVisitor;
import sk.jacob.sql.dialect.Statement;

public class TYPE {
    public static abstract class Type extends Statement {
        public final Class columnType;
        public Type(Class columnType) {
            this.columnType = columnType;
        }
    }

    public static class StringType extends Type {
        public StringType() {
            super(String.class);
        }

        public Integer length;
        public StringType length(Integer length) {
            this.length = length;
            return this;
        }

        @Override
        public String sql(DialectVisitor visitor) {
            return visitor.visit(this);
        }
    }
    public static StringType String() {
        return new StringType();
    }

    public static class BooleanType extends Type {
        public BooleanType() {
            super(Boolean.class);
        }
        @Override
        public String sql(DialectVisitor visitor) {
            return visitor.visit(this);
        }
    }
    public static BooleanType Boolean() {
        return new BooleanType();
    }

    public static class LongType extends Type {
        public LongType() {
            super(Long.class);
        }
        @Override
        public String sql(DialectVisitor visitor) {
            return visitor.visit(this);
        }
    }
    public static LongType Long() {
        return new LongType();
    }
}
