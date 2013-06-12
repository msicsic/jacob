package sk.jacob.sql;

import sk.jacob.sql.dialect.CompiledStatementList;
import sk.jacob.sql.dialect.DialectVisitor;

public class Where extends Statement {
    public ConditionalOperation conditionalOperation;
    public Statement rootStatement;

    public Where(Statement rootStatement, ConditionalOperation conditionalOperation) {
        this.rootStatement = (rootStatement == null) ? this : rootStatement;
        this.conditionalOperation = conditionalOperation;
        this.conditionalOperation.paramCounter = new Statement.ParamCounter();
        this.rootStatement.paramCounter = this.conditionalOperation.paramCounter;
    }

    @Override
    public String sql(DialectVisitor visitor) {
        StringBuffer sb = new StringBuffer("WHERE ");
        String coSql = conditionalOperation.sql(visitor);
        sb.append(coSql);
        return sb.toString();
    }

    @Override
    public Statement rootStatement() {
        return this.rootStatement;
    }
}
