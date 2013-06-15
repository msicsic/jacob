package sk.jacob.sql;

import static sk.jacob.sql.CRUD.*;
import static sk.jacob.sql.DDL.*;
import static sk.jacob.sql.TYPE.*;
import static sk.jacob.sql.Column.options;

public class Main {
    public static void main(String[] args) {
        Statement statement = select("col1", "col2").
                               from("table1", "table2").
                               where(Op.and(Op.eq("col2", 2),
                                            Op.le("col4", "abc")));
        Statement.CompiledStatement compiledStatement = statement.compile();
        System.out.println(compiledStatement.compiledStatement());
        System.out.println(compiledStatement.parameters());

        Metadata metadata = new Metadata();
        table("m01_users", metadata,
                column("login", string().length(255), options().primaryKey(true)),
                column("username", string().length(255), options().nullable(false)),
                column("md5pwd", string().length(255), options().nullable(false)),
                column("token", string().length(255), options().nullable(true).unique(true)),
                column("admin", string().length(255), options().nullable(true)));
    }
}
