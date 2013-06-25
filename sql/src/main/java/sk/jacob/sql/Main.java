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
        System.out.println(compiledStatement.normalizedStatement());
        System.out.println(compiledStatement.parameterList());

        Metadata metadata = new Metadata();
        table("users", metadata,
                column("login", String().length(255), options().primaryKey()),
                column("username", String().length(255), options().nullable()),
                column("md5pwd", String().length(255), options().nullable()),
                column("token", String().length(255), options().nullable().unique()),
                column("admin", Boolean(), options().nullable()));

//        DbEngine dbEngine = new DbEngine("org.h2.Driver", "jdbc:h2:/data/test", "sa", "sa");
//        metadata.createAll(dbEngine);

        statement = insert("users").values(cv("login", "ADMIN"), cv("username", "Administrator"));
        compiledStatement = statement.compile();
        System.out.println("\n" + compiledStatement.compiledStatement());
        System.out.println(compiledStatement.parameters());
        System.out.println(compiledStatement.normalizedStatement());
        System.out.println(compiledStatement.parameterList());

        statement = delete("users").where(Op.eq("ADMIN", Boolean.TRUE));
        compiledStatement = statement.compile();
        System.out.println("\n" + compiledStatement.compiledStatement());
        System.out.println(compiledStatement.parameters());
        System.out.println(compiledStatement.normalizedStatement());
        System.out.println(compiledStatement.parameterList());
    }
}
