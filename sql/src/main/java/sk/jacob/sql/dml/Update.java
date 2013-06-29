package sk.jacob.sql.dml;

import sk.jacob.sql.dialect.DialectVisitor;

public class Update extends DMLStatement {
    public final String tableName;
    private Where where;

    public Update(String tableName) {
        this.tableName = tableName;
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
        return null;
    }
}
