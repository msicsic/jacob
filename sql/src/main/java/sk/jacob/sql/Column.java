package sk.jacob.sql;

import sk.jacob.sql.dialect.DDLStatement;
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

    public final TYPE.Type type;
    public final Options options;

    Column(String name, TYPE.Type type, Options options) {
        super(name);
        this.type = type;
        this.options = options;
    }

    public static Options options() {
        return new Options();
    }

    @Override
    public DDLStatement sql(DialectVisitor dialect) {
        return dialect.visit(this);
    }
}
