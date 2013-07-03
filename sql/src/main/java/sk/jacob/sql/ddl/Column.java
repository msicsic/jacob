package sk.jacob.sql.ddl;

import sk.jacob.sql.dml.DMLStatement;
import sk.jacob.sql.generator.IdGenerator;
import sk.jacob.sql.dialect.DialectVisitor;

public class Column extends DbObject {
    public static class Options extends DMLStatement {
        private Boolean primaryKey = Boolean.FALSE;
        private IdGenerator generator = null;
        public Options primaryKey() {
            return primaryKey(null);
        }
        public Options primaryKey(IdGenerator generator) {
            this.generator = generator;
            this.primaryKey = Boolean.TRUE;
            return this;
        }
        public Boolean isPrimaryKey() {
            return this.primaryKey;
        }
        public IdGenerator getGenerator() {
            return this.generator;
        }


        private Boolean nullable = Boolean.FALSE;
        public Options nullable() {
            this.nullable = Boolean.TRUE;
            return this;
        }
        public Boolean isNullable() {
            return this.nullable;
        }


        private Boolean unique = Boolean.FALSE;
        public Options unique() {
            this.unique = Boolean.TRUE;
            return this;
        }
        public Boolean isUnique() {
            return this.unique;
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

    Column(String name, TYPE.Type type) {
        super(name);
        this.type = type;
        this.options = null;
    }

    public static Options options() {
        return new Options();
    }

    @Override
    public DDLStatement create(DialectVisitor dialect) {
        return dialect.create(this);
    }
}
