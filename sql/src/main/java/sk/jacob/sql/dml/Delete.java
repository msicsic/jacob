package sk.jacob.sql.dml;

import sk.jacob.sql.ddl.Table;
import sk.jacob.sql.dialect.DialectVisitor;

public class Delete extends DMLClause implements DeleteClause {
    public final String tableName;
    private Where where;

    public Delete(String tableName) {
        this.tableName = tableName;
    }

    public Delete(Table table) {
        this.tableName = table.name;
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

    public Where getWhereClause() {
        return this.where;
    }

    @Override
    public String sql(DialectVisitor visitor) {
        return visitor.sql(this);
    }
}
