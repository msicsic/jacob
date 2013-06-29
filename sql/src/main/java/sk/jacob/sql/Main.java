package sk.jacob.sql;

import sk.jacob.sql.ddl.Sequence;
import sk.jacob.sql.ddl.Table;
import sk.jacob.sql.dml.DMLStatement;
import sk.jacob.sql.engine.DbEngine;
import sk.jacob.sql.engine.ExecutionContext;

import static sk.jacob.sql.dml.DML.*;
import static sk.jacob.sql.ddl.Column.options;
import static sk.jacob.sql.ddl.DDL.*;
import static sk.jacob.sql.dml.Function.count;
import static sk.jacob.sql.dml.Op.*;
import static sk.jacob.sql.generator.SequenceIdGenerator.sequenceIdGenerator;
import static sk.jacob.sql.ddl.TYPE.*;

public class Main {
    public static void main(String[] args) {
        DMLStatement dmlStatement = select(count("*"), "col1", "col2").
                              from("table1", "table2").
                              where(and(eq("col2", 2),
                                      le("col4", "abc")));
        dumpStatement(dmlStatement);

        dmlStatement = delete("users").where(eq("ADMIN", Boolean.TRUE));
        dumpStatement(dmlStatement);

        dmlStatement = insert("users").values(cv("username", "Administrator"));
        dumpStatement(dmlStatement);

        Metadata metadata = new Metadata();
        Sequence sequence = sequence("SEQ_A", metadata);
        Table users = table("users", metadata,
                column("login", Long(), options().primaryKey(sequenceIdGenerator(sequence))),
                column("username", String(255), options().nullable()),
                column("md5pwd", String(255), options().nullable()),
                column("token", String(255), options().nullable().unique()),
                column("admin", Boolean(), options().nullable()));

        DbEngine dbEngine = new DbEngine("org.h2.Driver", "jdbc:h2:data/test", "sa", "sa");
        metadata.createAll(dbEngine);

        dmlStatement = insert(users).values(cv("username", "Administrator"));
        ExecutionContext ectx = dbEngine.getExecutionContext();
        ectx.txBegin();
        System.out.println(ectx.execute(dmlStatement));
        ectx.txCommit();
        ectx.close();
    }

    private static void dumpStatement(DMLStatement dmlStatement) {
        DMLStatement.CompiledStatement compiledStatement = dmlStatement.compile();
        System.out.println(compiledStatement.compiledStatement());
        System.out.println(compiledStatement.parameters());
        System.out.println(compiledStatement.normalizedStatement());
        System.out.println(compiledStatement.parameterList());
    }
}
