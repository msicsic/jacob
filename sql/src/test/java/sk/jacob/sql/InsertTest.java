package sk.jacob.sql;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sk.jacob.sql.Model.TestModel;
import sk.jacob.sql.ddl.DDLStatement;
import sk.jacob.sql.ddl.DbObject;
import sk.jacob.sql.ddl.Table;
import sk.jacob.sql.dml.DML;
import sk.jacob.sql.dml.SqlClause;
import sk.jacob.sql.engine.Connection;
import sk.jacob.sql.engine.DbEngine;

import static org.junit.Assert.fail;
import static sk.jacob.sql.dml.DML.cv;

public class InsertTest {
    private static final Metadata MODEL = TestModel.METADATA;
    private static final DbEngine engine = new DbEngine("jdbc:h2:/jacobTest/testModel", "sa", "sa");
    private Connection connection;

    @Before
    public void setup() {
        MODEL.createAll(engine);
        connection = engine.getConnection();
    }

    @After
    public void tearDown() {
        connection.close();
        connection = null;
        MODEL.dropAll(engine);
    }

    @Test
    public void nullableTestPlain() {
        SqlClause notNullableInsert = DML.insert("TAB_NULLABLE")
                                         .values(cv("C_STRING_NOT_NULLABLE", "C_STRING_NOT_NULLABLE_VALUE"));

        connection.txBegin();
        connection.execute(notNullableInsert);
        connection.txCommit();

        SqlClause nullableInsert = DML.insert("TAB_NULLABLE")
                                      .values(cv("C_STRING_NULLABLE", "C_STRING_NULLABLE"));
        try {
            connection.txBegin();
            connection.execute(nullableInsert);
            connection.txCommit();
            fail("NULL not allowed for column.");
        } catch (Exception e) {
            // pass
        }
    }

    @Test
    public void uniqueTestPlain() {
        SqlClause uniqueInsert = DML.insert("TAB_UNIQUE")
                                    .values(cv("C_STRING_UNIQUE", "STRING_ABC"),
                                            cv("C_STRING_NOT_UNIQUE", "STRING_XZY"));

        connection.txBegin();
        connection.execute(uniqueInsert);
        connection.txCommit();

        SqlClause uniqueInsert2 = DML.insert("TAB_UNIQUE")
                                     .values(cv("C_STRING_UNIQUE", "STRING_DEF"),
                                             cv("C_STRING_NOT_UNIQUE", "STRING_XZY"));

        connection.txBegin();
        connection.execute(uniqueInsert2);
        connection.txCommit();

        try {
            connection.txBegin();
            connection.execute(uniqueInsert2);
            connection.txCommit();
            fail("NOT UNIQUE not allowed for column.");
        } catch (Exception e) {
            // pass
        }
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
