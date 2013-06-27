package sk.jacob.sql.dml;

import sk.jacob.sql.dialect.DialectVisitor;
import sk.jacob.sql.dialect.Statement;

import java.util.Arrays;
import java.util.List;

public class From extends Statement {
    public final List<String> tableNames;
    private Where where;

    public From(Statement parentStatement, String ... tableNames) {
        super(parentStatement);
        this.tableNames = Arrays.asList(tableNames);
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
        return visitor.visit(this);
    }
}
