package sk.jacob.sql;

import sk.jacob.sql.ddl.Sequence;
import sk.jacob.sql.ddl.Table;
import sk.jacob.sql.dialect.Statement;
import sk.jacob.sql.engine.DbEngine;
import sk.jacob.sql.engine.ExecutionContext;

import static sk.jacob.sql.dml.DML.*;
import static sk.jacob.sql.ddl.Column.options;
import static sk.jacob.sql.ddl.DDL.*;
import static sk.jacob.sql.dml.Op.*;
import static sk.jacob.sql.generator.SequenceIdGenerator.sequenceIdGenerator;
import static sk.jacob.sql.ddl.TYPE.*;

public class Main {
    public static void main(String[] args) {
        Statement statement = select("col1", "col2").
                              from("table1", "table2").
                              where(and(eq("col2", 2),
                                        le("col4", "abc")));
        dumpStatement(statement);

        statement = delete("users").where(eq("ADMIN", Boolean.TRUE));
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
