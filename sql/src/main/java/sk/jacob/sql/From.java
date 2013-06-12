package sk.jacob.sql;

import sk.jacob.sql.dialect.DialectVisitor;

import java.util.Arrays;
import java.util.List;

public class From extends Statement {
    public List<String> tableNames;
    public Where where;

    public From(Statement rootStatement, String ... tableNames) {
        super(rootStatement);
        this.tableNames = Arrays.asList(tableNames);
    }

    public Where where(ConditionalOperation conditionalOperation) {
        return where( new Where(this.getRootStatement(), conditionalOperation) );
    }

    public Where where(Where where) {
        this.where = where;
        return this.where;
    }

    @Override
    public String sql(DialectVisitor visitor) {
        String from = visitor.visit(this);
        StringBuffer sb = new StringBuffer(from);
        if(this.where != null) {
            sb.append("\n");
            String whereSql = this.where.sql(visitor);
            sb.append(whereSql);
        }
        return sb.toString();
    }
}
