package sk.jacob.sql.dml;

public interface SelectClause extends SqlClause {
    FromClause from(Object... tableExpressions);
    FromClause from(From from);
}
