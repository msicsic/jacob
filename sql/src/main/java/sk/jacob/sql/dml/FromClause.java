package sk.jacob.sql.dml;

public interface FromClause extends SqlClause {
    WhereClause where(ConditionalOperation conditionalOperation);
    WhereClause where(Where where);
    FromClause join(Object tableExpression, ConditionalOperation co);
    FromClause leftJoin(Object tableExpression, ConditionalOperation co);
}
