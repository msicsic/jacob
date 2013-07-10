package sk.jacob.sql.dml;

import sk.jacob.sql.dialect.DialectVisitor;
import sk.jacob.sql.dialect.GenericDialectVisitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class From extends DMLClause implements FromClause {
    public final List<Object> tableExpressions;
    public final List<Join> joins = new ArrayList<>();
    private Where where;

    public From(DMLClause parentDMLClause, Object ... tableExpressions) {
        super(parentDMLClause);
        this.tableExpressions = Arrays.asList(tableExpressions);
    }

    @Override
    public WhereClause where(ConditionalOperation conditionalOperation) {
        return where( new Where(this, conditionalOperation) );
    }

    @Override
    public WhereClause where(Where where) {
        this.where = where;
        return this.where;
    }

    @Override
    public FromClause leftJoin(Object joinExpression, ConditionalOperation co) {
        joins.add( new LeftJoin(this, joinExpression, co));
        return this;
    }

    @Override
    public FromClause join(Object joinExpression, ConditionalOperation co) {
        joins.add( new InnerJoin(this, joinExpression, co));
        return this;
    }

    @Override
    public String sql(DialectVisitor visitor) {
        return visitor.sql(this);
    }

    public Where getWhereClause() {
        return this.where;
    }
}
