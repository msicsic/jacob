package sk.jacob.sql;

import sk.jacob.sql.dialect.DDLStatement;
import sk.jacob.sql.dialect.DialectVisitor;

public class Column extends DbObject {
    public static class Options extends Statement {
        public Boolean primaryKey = Boolean.FALSE;
        public Options primaryKey() {
            this.primaryKey = Boolean.TRUE;
            return this;
        }

        public Boolean nullable = Boolean.FALSE;
        public Options nullable() {
            this.nullable = Boolean.TRUE;
            return this;
        }

        public Boolean unique = Boolean.FALSE;
        public Options unique() {
            this.unique = Boolean.TRUE;
            return this;
        }

        @Override
        public String sql(DialectVisitor dialect) {
            return dialect.visit(this);
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
