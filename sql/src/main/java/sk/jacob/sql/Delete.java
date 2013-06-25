package sk.jacob.sql;

import sk.jacob.sql.dialect.DialectVisitor;

public class Delete extends Statement {
    public final String tableName;
    public Where where;

    public Delete(String tableName) {
        this.tableName = tableName;
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
