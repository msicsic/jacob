package sk.jacob.sql.dml;

import sk.jacob.sql.dialect.DialectVisitor;
import sk.jacob.sql.dialect.GenericDialectVisitor;

public class InnerJoin extends Join {
    public InnerJoin(DMLClause parentClause, Object tableExpression, ConditionalOperation conditionalOperation) {
        super(parentClause, tableExpression, conditionalOperation);
    }

    @Override
    public String sql(DialectVisitor visitor) {
        return visitor.sql(this);
    }
}
