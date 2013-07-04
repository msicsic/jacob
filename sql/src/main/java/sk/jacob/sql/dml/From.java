package sk.jacob.sql.dml;

import sk.jacob.sql.dialect.DialectVisitor;

import java.util.Arrays;
import java.util.List;

public class From extends DMLStatement {
    public final List<Object> tableExpressions;
    private Where where;

    public From(DMLStatement parentDMLStatement, Object ... tableExpressions) {
        super(parentDMLStatement);
        this.tableExpressions = Arrays.asList(tableExpressions);
    }

    public Where where(ConditionalOperation conditionalOperation) {
        return where( new Where(this, conditionalOperation) );
    }

    public Where where(Where where) {
        this.where = where;
        return this.where;
    }

    public Where getWhereClause() {
        return this.where;
    }

    @Override
    public String sql(DialectVisitor visitor) {
        return visitor.sql(this);
    }
}
