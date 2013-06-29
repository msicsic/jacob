package sk.jacob.sql.dml;

import sk.jacob.sql.dialect.DialectVisitor;

import java.util.Arrays;
import java.util.List;

public class Select extends DMLStatement {
    public final List<Object> columnExpressions;
    private From from;

    public Select(Object ... columnExpressions) {
        super();
        this.columnExpressions = Arrays.asList(columnExpressions);
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

