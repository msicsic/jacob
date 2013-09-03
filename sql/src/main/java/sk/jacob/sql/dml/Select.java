package sk.jacob.sql.dml;

import sk.jacob.sql.dialect.DialectVisitor;

import java.util.Arrays;
import java.util.List;

public class Select extends DMLClause implements SelectClause {
    public final List<Object> columnExpressions;
    private From from;

    public Select(Object ... columnExpressions) {
        super();
        this.columnExpressions = Arrays.asList(columnExpressions);
    }

    @Override
    public FromClause from(Object... tableExpressions) {
        return from( new From(this, tableExpressions) );
    }

    @Override
    public FromClause from(From from) {
        this.from = from;
        return this.from;
    }

    @Override
    public String sql(DialectVisitor visitor){
        return visitor.sql(this);
    }

    public From getFromClause() {
        return this.from;
    }
}

