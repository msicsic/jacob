package sk.jacob.sql;

import sk.jacob.sql.dialect.CompiledStatementList;
import sk.jacob.sql.dialect.DialectVisitor;

import java.util.Arrays;
import java.util.List;

public class Select extends Statement {
    public final List<String> columnNames;
    public From from;
    public Statement rootStatement;

    public Select(String ... columnNames) {
        this.columnNames = Arrays.asList(columnNames);
        this.rootStatement = this;
    }

    public From from(String ... tableNames) {
        return from( new From(this.rootStatement, tableNames) );
    }

    public From from(From from) {
        this.from = from;
        return this.from;
    }

    public String sql(DialectVisitor visitor){
        String select = visitor.visit(this);
        StringBuffer sb = new StringBuffer(select);
        sb.append("\n");
        if(from != null) {
            String from = this.from.sql(visitor);
            sb.append(from.toString());
        }
        sb.append(";");
        return sb.toString();
    }

    @Override
    public Statement rootStatement() {
        return this.rootStatement;
    }
}
