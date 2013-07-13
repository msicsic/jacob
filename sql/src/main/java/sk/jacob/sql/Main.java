package sk.jacob.sql;

import sk.jacob.sql.ddl.*;
import sk.jacob.sql.dml.DMLClause;
import sk.jacob.sql.dml.SqlClause;

import static sk.jacob.sql.ddl.DDL.column;
import static sk.jacob.sql.dml.DML.*;
import static sk.jacob.sql.ddl.Column.options;
import static sk.jacob.sql.ddl.DDL.*;
import static sk.jacob.sql.dml.Function.count;
import static sk.jacob.sql.dml.Op.*;
import static sk.jacob.sql.generator.SequenceIdGenerator.sequenceIdGenerator;
import static sk.jacob.sql.ddl.TYPE.*;

public class Main {
    public static void main(String[] args) {
        SqlClause sqlClause = null;
        Metadata metadata = new Metadata();
        sqlClause = select(count("*"), "col1 AS FOO", "col2").
                              from("table1", "table2").
                              leftJoin("table3", eq("table1.id", column("table3.fk"))).
                              join("table4", eq("table1.id", column("table4.fk"))).
                              where(and(eq("col2", 2),
                                      le("col4", "abc"),
                                      eq("col6", true)));
        dumpClause(sqlClause);
//
//        sqlClause = delete("users").where(eq("ADMIN", true));
//        dumpClause(sqlClause);
//
//        sqlClause = insert("users").values(cv("username", "Administrator"));
//        dumpClause(sqlClause);
//
//        sqlClause = update("users").set(cv("token", null)).where(eq("token", "TOKEN_STRING"));
//        dumpClause(sqlClause);
//
//        Sequence sequence = sequence("SEQ_A", metadata);
//        dumpObject(sequence);
//
//        Table users =
//                table("users", metadata,
//                column("login", Long(), options().primaryKey(sequenceIdGenerator(sequence))),
//                column("username", String(255), options().foreignKey("refTable.refColumn")),
//                column("md5pwd", String(255), options().nullable(false)),
//                column("token", String(255), options().nullable(false).unique(true)),
//                column("admin", Boolean(), options().nullable(false)));
//        dumpObject(users);
//
        Users2 users2 = new Users2(metadata);
        //dumpObject(users2);

        sqlClause = select(count("*"), users2.login, "col2")
                       .from("table1", users2).join(users2, eq(users2.login, users2.username))
                       .where(and(eq("col2", 2),
                               le("col4", "abc")));
        dumpClause(sqlClause);
    }

    private static class Users2 extends Table {
        public static final String NAME = "USERS2";
        public Users2(Metadata metadata) {
            super(NAME, metadata);
        }

        public final Column login = new Column(this,
                                               "login",
                                               Long(),
                                               options().nullable(false));

        public final Column username = new Column(this, "username",
                                                  String(255),
                                                  options().foreignKey("refTable.refColumn"));
    }

    private static void dumpClause(SqlClause sqlClause) {
        System.out.println(((DMLClause)sqlClause).dump());
    }

    private static void dumpObject(DbObject dbObject) {
        System.out.println(dbObject.dump());
    }
}
