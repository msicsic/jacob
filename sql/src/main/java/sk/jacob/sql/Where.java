package sk.jacob.sql;

import sk.jacob.sql.dialect.DialectVisitor;

public class Where extends Statement {
    public ConditionalOperation conditionalOperation;

    public Where(Statement rootStatement, ConditionalOperation conditionalOperation) {
        super(rootStatement);
        this.conditionalOperation = conditionalOperation;
        this.conditionalOperation.setRootStatement(rootStatement);
    }

    @Override
    public String sql(DialectVisitor visitor) {
        StringBuffer sb = new StringBuffer("WHERE ");
        String coSql = conditionalOperation.sql(visitor);
        sb.append(coSql);
        return sb.toString();
    }
}
