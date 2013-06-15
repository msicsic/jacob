package sk.jacob.sql;

import sk.jacob.sql.dialect.CompiledStatementList;
import sk.jacob.sql.dialect.DialectVisitor;

public class Column extends DbObject {
    public static class Options {
        public Boolean primaryKey;
        public Options primaryKey(Boolean primaryKey) {
            this.primaryKey = primaryKey;
            return this;
        }

        public Boolean nullable;
        public Options nullable(Boolean nullable) {
            this.nullable = nullable;
            return this;
        }

        public Boolean unique;
        public Options unique(boolean unique) {
            this.unique = unique;
            return this;
        }
    }

    public final String name;
    public final TYPE.Type type;
    public Options options;

    Column(String name, TYPE.Type type, Options options) {
        this.name = name;
        this.type = type;
        this.options = options;
    }

    public static Options options() {
        return new Options();
    }

    @Override
    public String sql(DialectVisitor dialect) {
        return null; //dialect.visit(this);
    }
}
