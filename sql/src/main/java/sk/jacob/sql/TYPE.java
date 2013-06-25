package sk.jacob.sql;
public class TYPE {
    public static abstract class Type{
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
    }
    public static StringType String() {
        return new StringType();
    }

    public static class BooleanType extends Type {
        public BooleanType() {
            super(Boolean.class);
        }
    }
    public static BooleanType Boolean() {
        return new BooleanType();
    }
}
