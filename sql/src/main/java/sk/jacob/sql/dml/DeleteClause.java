package sk.jacob.sql.dml;

public interface DeleteClause {
    WhereClause where(ConditionalOperation conditionalOperation);
    WhereClause where(Where where);
}
