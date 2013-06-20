package sk.jacob.sql;

import static sk.jacob.sql.CRUD.*;
import static sk.jacob.sql.DDL.*;
import static sk.jacob.sql.TYPE.*;
import static sk.jacob.sql.Column.options;
import static sk.jacob.sql.Op.*;

public class Main {
    public static void main(String[] args) {
        Statement statement = select("col1", "col2").
                               from("table1", "table2").
                               where(and(eq("col2", 2),
                                         le("col4", "abc")));
        Statement.CompiledStatement compiledStatement = statement.compile();
        System.out.println(compiledStatement.compiledStatement());
        System.out.println(compiledStatement.parameters());
        System.out.println(compiledStatement.positionalParameters());
        System.out.println(compiledStatement.normalizedStatement());

        Metadata metadata = new Metadata();
        table("users", metadata,
                column("login", string().length(255), options().primaryKey(true)),
                column("username", string().length(255), options().nullable(false)),
                column("md5pwd", string().length(255), options().nullable(false)),
                column("token", string().length(255), options().nullable(true).unique(true)),
                column("admin", string().length(1), options().nullable(true)));

//        DbEngine dbEngine = new DbEngine("org.h2.Driver", "jdbc:h2:/data/test", "sa", "sa");
//        metadata.createAll(dbEngine);


        statement = insert("users").values(cv("login", "ADMIN"), cv("username", "Administrator"));
        compiledStatement = statement.compile();
        System.out.println("\n" + compiledStatement.compiledStatement());
        System.out.println(compiledStatement.parameters());
        System.out.println(compiledStatement.positionalParameters());
        System.out.println(compiledStatement.normalizedStatement());

    }
}
