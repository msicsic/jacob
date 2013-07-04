package sk.jacob.sql;

import sk.jacob.sql.ddl.DDLStatement;
import sk.jacob.sql.ddl.DbObject;
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

        dmlStatement = update("users").set(cv("token", null)).where(eq("token", "TOKEN_STRING"));
        dumpStatement(dmlStatement);

        Metadata metadata = new Metadata();
        Sequence sequence = sequence("SEQ_A", metadata);
        dumpObject(sequence);

        Table users =
                table("users", metadata,
                column("login", Long(), options().primaryKey(sequenceIdGenerator(sequence))),
                column("username", String(255), options().foreignKey("refTable.refColumn")),
                column("md5pwd", String(255), options().nullable(false)),
                column("token", String(255), options().nullable(false).unique(true)),
                column("admin", Boolean(), options().nullable(false)));
        dumpObject(users);

    }

    private static void dumpStatement(DMLStatement dmlStatement) {
        DMLStatement.CompiledStatement compiledStatement = dmlStatement.compile();
        System.out.println(">>>");
        System.out.println(compiledStatement.compiledStatement());
        System.out.println(compiledStatement.parameters());
        System.out.println(compiledStatement.normalizedStatement());
        System.out.println(compiledStatement.parameterList());
    }

    private static void dumpObject(DbObject dbObject) {
        DDLStatement statement  = dbObject.create();
        System.out.println(">>>");
        System.out.println(statement.inline);
        for(String outline : statement.outline) {
            System.out.println(outline);
        }
    }
}
