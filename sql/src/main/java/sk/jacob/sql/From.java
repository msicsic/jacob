package sk.jacob.sql;

import sk.jacob.sql.dialect.DialectVisitor;

import java.util.Arrays;
import java.util.List;

public class From implements Statement {
    private Query query;
    public List<String> tableNames;

    public From(Query query) {
        this.query = query;
    }

    public Where from(String ... tableNames) {
        this.tableNames = Arrays.asList(tableNames);
        return new Where(this);
    }

    @Override
    public String sql(DialectVisitor visitor) {
        StringBuffer b = new StringBuffer(query.sql(visitor));
        b.append(" ");
        b.append(visitor.visit(this));
        return b.toString();
    }
}
