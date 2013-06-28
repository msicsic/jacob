package sk.jacob.sql.dml;

import sk.jacob.sql.dialect.DialectVisitor;
import sk.jacob.sql.dialect.Statement;
import sk.jacob.sql.dml.From;

import java.util.Arrays;
import java.util.List;

public class Select extends Statement {
    public final List<Object> columnPredicates;
    private From from;

    public Select(Object ... columnPredicates) {
        super();
        this.columnPredicates = Arrays.asList(columnPredicates);
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

