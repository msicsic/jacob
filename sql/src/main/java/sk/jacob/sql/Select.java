package sk.jacob.sql;

import sk.jacob.sql.dialect.DialectVisitor;

import java.util.Arrays;
import java.util.List;

public class Select extends Statement {
    public final List<String> columnNames;
    public From from;

    public Select(String ... columnNames) {
        super();
        this.columnNames = Arrays.asList(columnNames);
    }

    public From from(String ... tableNames) {
        return from( new From(this, tableNames) );
    }

    public From from(From from) {
        this.from = from;
        return this.from;
    }

    public String sql(DialectVisitor visitor){
        String select = visitor.visit(this);
        StringBuffer sb = new StringBuffer(select);
        sb.append("\n");
        if(this.from != null) {
            String fromSql = this.from.sql(visitor);
            sb.append(fromSql);
        }
        sb.append(";");
        return sb.toString();
    }
}
