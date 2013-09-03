package sk.jacob.sql.dml;

import sk.jacob.sql.dialect.DialectVisitor;

public class Where extends DMLClause implements WhereClause {
    public final ConditionalOperation conditionalOperation;

    public Where(DMLClause parentDMLClause, ConditionalOperation conditionalOperation) {
        super(parentDMLClause);
        this.conditionalOperation = conditionalOperation;
        this.conditionalOperation.setParentStatement(this);
    }

    @Override
    public String sql(DialectVisitor visitor) {
        return visitor.sql(this);
    }
}
