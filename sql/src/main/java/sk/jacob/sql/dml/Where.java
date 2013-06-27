package sk.jacob.sql.dml;

import sk.jacob.sql.dialect.DialectVisitor;
import sk.jacob.sql.dialect.Statement;

public class Where extends Statement {
    public final ConditionalOperation conditionalOperation;

    public Where(Statement parentStatement, ConditionalOperation conditionalOperation) {
        super(parentStatement);
        this.conditionalOperation = conditionalOperation;
        this.conditionalOperation.setParentStatement(this);
    }

    @Override
    public String sql(DialectVisitor visitor) {
        return visitor.visit(this);
    }
}
