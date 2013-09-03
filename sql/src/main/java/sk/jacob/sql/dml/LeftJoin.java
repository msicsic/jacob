package sk.jacob.sql.dml;

import sk.jacob.sql.dialect.DialectVisitor;

public class LeftJoin extends Join {
    public LeftJoin(DMLClause parentClause, Object tableExpression, ConditionalOperation conditionalOperation) {
        super(parentClause, tableExpression, conditionalOperation);
    }

    @Override
    public String sql(DialectVisitor visitor) {
        return visitor.sql(this);
    }
}
