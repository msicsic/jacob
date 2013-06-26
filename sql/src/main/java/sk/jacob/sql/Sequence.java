package sk.jacob.sql;

import sk.jacob.sql.dialect.DDLStatement;
import sk.jacob.sql.dialect.DialectVisitor;

public class Sequence extends DbObject {
    public Sequence(String sequenceName, Metadata metadata) {
        super(sequenceName);
        metadata.add(this);
    }

    @Override
    public DDLStatement sql(DialectVisitor visitor) {
        return visitor.visit(this);
    }

    public Long nextVal(DbEngine dbEngine) {
        DialectVisitor visitor = dbEngine.getDialect();
        String nextValStatement = visitor.sequenceNextVal(this);
        return dbEngine.getExecutionContext().execute(nextValStatement);
    }
}
