package sk.jacob.sql;

import sk.jacob.sql.dialect.DialectVisitor;

import java.util.Arrays;
import java.util.List;

public class Query implements Statement {
    public List<String> columnNames;

    private Query(String ... columnNames) {
        this.columnNames = Arrays.asList(columnNames);
    }

    public String sql(DialectVisitor visitor){
        return visitor.visit(this);
    }

    public static From select(String ... columnNames) {
        return new From(new Query(columnNames));
    }
}
