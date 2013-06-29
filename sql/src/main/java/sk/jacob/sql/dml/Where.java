package sk.jacob.sql.dml;

import sk.jacob.sql.dialect.DialectVisitor;

public class Where extends DMLStatement {
    public final ConditionalOperation conditionalOperation;

    public Where(DMLStatement parentDMLStatement, ConditionalOperation conditionalOperation) {
        super(parentDMLStatement);
        this.conditionalOperation = conditionalOperation;
        this.conditionalOperation.setParentStatement(this);
    }

    @Override
    public String sql(DialectVisitor visitor) {
        return visitor.visit(this);
    }
}
