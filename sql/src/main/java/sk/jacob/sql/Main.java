package sk.jacob.sql;

public class Main {
    public static void main(String[] args) {
        Statement statement = CRUD.select("col1", "col2").
                               from("table1", "table2").
                               where(Op.and(Op.eq("col2", 2),
                                            Op.le("col4", "abc")));
        Statement.CompiledStatement compiledStatement = statement.compile();
        System.out.println(compiledStatement.compiledStatement());
        System.out.println(compiledStatement.parameters());
    }
}
