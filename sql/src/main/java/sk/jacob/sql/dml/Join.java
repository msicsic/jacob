package sk.jacob.sql.dml;

public abstract class Join extends DMLClause {
    public final Object tableExpression;
    public final ConditionalOperation conditionalOperation;

    public Join(DMLClause parentClause, Object tableExpression, ConditionalOperation conditionalOperation) {
        super(parentClause);
        this.tableExpression = tableExpression;
        this.conditionalOperation = conditionalOperation;
        this.conditionalOperation.setParentStatement(parentClause);
    }
}
