package sk.jacob.sql.ddl;

import sk.jacob.sql.Metadata;
import sk.jacob.sql.ddl.DbObject;
import sk.jacob.sql.dialect.DDLStatement;
import sk.jacob.sql.dialect.DialectVisitor;
import sk.jacob.sql.engine.DbEngine;
import sk.jacob.sql.engine.ExecutionContext;

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
        ExecutionContext ectx = dbEngine.getExecutionContext();
        Long nextVal = ectx.execute(nextValStatement);
        ectx.close();
        return nextVal;
    }
}
