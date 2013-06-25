package sk.jacob.sql;

import sk.jacob.sql.dialect.DialectVisitor;

import java.util.Arrays;
import java.util.List;

public class From extends Statement {
    public List<String> tableNames;
    public Where where;

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

    @Override
    public String sql(DialectVisitor visitor) {
        return visitor.visit(this);
    }
}
