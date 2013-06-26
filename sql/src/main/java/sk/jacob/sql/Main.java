package sk.jacob.sql;

import sk.jacob.sql.dialect.Statement;

import static sk.jacob.sql.CRUD.*;
import static sk.jacob.sql.CRUD.insert;
import static sk.jacob.sql.Column.options;
import static sk.jacob.sql.DDL.column;
import static sk.jacob.sql.DDL.sequence;
import static sk.jacob.sql.DDL.table;
import static sk.jacob.sql.Op.and;
import static sk.jacob.sql.Op.eq;
import static sk.jacob.sql.Op.le;
import static sk.jacob.sql.SequenceIdGenerator.sequenceIdGenerator;
import static sk.jacob.sql.TYPE.Boolean;
import static sk.jacob.sql.TYPE.String;
import static sk.jacob.sql.TYPE.Long;

public class Main {
    public static void main(String[] args) {
        Statement statement = select("col1", "col2").
                              from("table1", "table2").
                              where(and(eq("col2", 2),
                                        le("col4", "abc")));
        dumpStatement(statement);

        statement = delete("users").where(Op.eq("ADMIN", Boolean.TRUE));
        dumpStatement(statement);

        statement = insert("users").values(cv("username", "Administrator"));
        dumpStatement(statement);

        Metadata metadata = new Metadata();
        Sequence sequence = sequence("SEQ_A", metadata);
        Table users = table("users", metadata,
                column("login", Long(), options().primaryKey(sequenceIdGenerator(sequence))),
                column("username", String().length(255), options().nullable()),
                column("md5pwd", String().length(255), options().nullable()),
                column("token", String().length(255), options().nullable().unique()),
                column("admin", Boolean(), options().nullable()));

        DbEngine dbEngine = new DbEngine("org.h2.Driver", "jdbc:h2:data/test", "sa", "sa");
        metadata.createAll(dbEngine);

        statement = insert(users).values(cv("username", "Administrator"));
        ExecutionContext ectx = dbEngine.getExecutionContext();
        ectx.txBegin();
        ectx.execute(statement);
        ectx.txCommit();
    }

    private static void dumpStatement(Statement statement) {
        Statement.CompiledStatement compiledStatement = statement.compile();
        System.out.println(compiledStatement.compiledStatement());
        System.out.println(compiledStatement.parameters());
        System.out.println(compiledStatement.normalizedStatement());
        System.out.println(compiledStatement.parameterList());
    }
}
