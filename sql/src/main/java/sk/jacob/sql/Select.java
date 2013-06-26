package sk.jacob.sql;

import sk.jacob.sql.dialect.DialectVisitor;
import sk.jacob.sql.dialect.Statement;

import java.util.Arrays;
import java.util.List;

public class Select extends Statement {
    public final List<String> columnNames;
    private From from;

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

    public From getFromClause() {
        return this.from;
    }

    @Override
    public String sql(DialectVisitor visitor){
        return visitor.visit(this);
    }
}

