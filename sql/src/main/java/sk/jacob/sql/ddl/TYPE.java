package sk.jacob.sql.ddl;

import sk.jacob.sql.dialect.DialectVisitor;
import sk.jacob.sql.dml.DMLClause;

public class TYPE {
    public static abstract class Type extends DMLClause {
        public final Class columnType;
        public Type(Class columnType) {
            this.columnType = columnType;
        }
    }

    public static class StringType extends Type {
        public final Integer length;
        public StringType(Integer length) {
            super(String.class);
            this.length = length;
        }

        @Override
        public String sql(DialectVisitor visitor) {
            return visitor.sql(this);
        }
    }
    public static StringType String(Integer length) {
        return new StringType(length);
    }

    public static class BooleanType extends Type {
        public BooleanType() {
            super(Boolean.class);
        }
        @Override
        public String sql(DialectVisitor visitor) {
            return visitor.sql(this);
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
            return visitor.sql(this);
        }
    }
    public static LongType Long() {
        return new LongType();
    }
}
