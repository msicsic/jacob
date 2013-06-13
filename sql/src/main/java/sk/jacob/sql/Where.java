package sk.jacob.sql;

import sk.jacob.sql.dialect.DialectVisitor;

public class Where extends Statement {
    public ConditionalOperation conditionalOperation;

    public Where(Statement parentStatement, ConditionalOperation conditionalOperation) {
        super(parentStatement);
        this.conditionalOperation = conditionalOperation;
        this.conditionalOperation.setParentStatement(this);
    }

    @Override
    public String sql(DialectVisitor visitor) {
        StringBuffer sb = new StringBuffer("WHERE ");
        String coSql = conditionalOperation.sql(visitor);
        sb.append(coSql);
        return sb.toString();
    }
}
