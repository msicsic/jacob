package sk.jacob.sql;

import org.junit.Test;
import sk.jacob.sql.dml.DML;
import sk.jacob.sql.dml.SqlClause;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static sk.jacob.sql.dml.DML.cv;

public class InsertPlainTest extends BaseTest {
    @Test
    public void nullableTest() {
        SqlClause notNullableInsert = DML.insert("TABP_NULLABLE")
                                         .values(cv("C_STRING_NOT_NULLABLE", "C_STRING_NOT_NULLABLE_VALUE"));
        execInTx(notNullableInsert);
        SqlClause nullableInsert = DML.insert("TABP_NULLABLE")
                                      .values(cv("C_STRING_NULLABLE", "C_STRING_NULLABLE"));
        try {
            execInTx(nullableInsert);
            fail("NULL not allowed for column.");
        } catch (Exception e) {
            // pass
        }
    }

    @Test
    public void uniqueTest() {
        SqlClause uniqueInsert = DML.insert("TABP_UNIQUE")
                                    .values(cv("C_STRING_UNIQUE", "STRING_ABC"),
                                            cv("C_STRING_NOT_UNIQUE", "STRING_XZY"));
        execInTx(uniqueInsert);
        SqlClause uniqueInsert2 = DML.insert("TABP_UNIQUE")
                                     .values(cv("C_STRING_UNIQUE", "STRING_DEF"),
                                             cv("C_STRING_NOT_UNIQUE", "STRING_XZY"));
        execInTx(uniqueInsert2);
        try {
            execInTx(uniqueInsert2);
            fail("NOT UNIQUE not allowed for column.");
        } catch (Exception e) {
            // pass
        }
    }

    @Test
    public void idInsertExplicit() {
        SqlClause ins = DML.insert("TABP_ID_INSERT")
                           .values(cv("C_ID", 1L),
                                   cv("C_STRING_VALUE", "C_STRING_VALUE"));
        long id = (long)execInTx(ins);
        assertEquals(1, id);
    }

    @Test
    public void idInsertImplicit() {
        SqlClause ins = DML.insert("TABP_ID_INSERT")
                           .values(cv("C_STRING_VALUE", "C_STRING_VALUE"));
        long id = (long)execInTx(ins);
        assertEquals(1, id);
    }

    @Test
    public void insertValidReference() {
        SqlClause ins = DML.insert("TABP_ID_INSERT")
                           .values(cv("C_ID", 1L),
                                   cv("C_STRING_VALUE", "C_STRING_VALUE"));
        execInTx(ins);
        SqlClause insRef = DML.insert("TABP_REFERENCING")
                              .values(cv("C_FK", 1L));
        execInTx(insRef);
    }

    @Test
    public void insertInvalidReference() {
        SqlClause ins = DML.insert("TABP_ID_INSERT")
                           .values(cv("C_ID", 1L),
                                   cv("C_STRING_VALUE", "C_STRING_VALUE"));
        execInTx(ins);
        SqlClause insRef = DML.insert("TABP_REFERENCING")
                              .values(cv("C_FK", 2L));
        try {
            execInTx(insRef);
            fail("INVALID REFERENCE not allowed for column.");
        } catch (Exception e) {
            // pass
        }
    }

    @Test
    public void sequenceEqualization() {
        SqlClause ins = DML.insert("TABP_ID_INSERT")
                           .values(cv("C_ID", 25L),
                                   cv("C_STRING_VALUE", "C_STRING_VALUE"));
        long id = (long)execInTx(ins);
        assertEquals(25, id);

        ins = DML.insert("TABP_ID_INSERT")
                 .values(cv("C_STRING_VALUE", "C_STRING_VALUE"));

        id = (long)execInTx(ins);
        assertEquals(26, id);
    }
}
