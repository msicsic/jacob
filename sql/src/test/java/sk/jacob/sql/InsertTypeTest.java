package sk.jacob.sql;

import org.junit.Test;
import sk.jacob.sql.dml.DML;
import sk.jacob.sql.dml.SqlClause;
import sk.jacob.sql.model.tables.TabTIdInsert;
import sk.jacob.sql.model.tables.TabTNullable;
import sk.jacob.sql.model.tables.TabTReferencing;
import sk.jacob.sql.model.tables.TabTUnique;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static sk.jacob.sql.dml.DML.cv;

public class InsertTypeTest extends BaseTest {
    @Test
    public void nullableTest() {
        TabTNullable table = MODEL.table(TabTNullable.class);
        SqlClause notNullableInsert = DML.insert(table)
                                         .values(cv(table.cStringNotNullable, "C_STRING_NOT_NULLABLE_VALUE"));
        execInTx(notNullableInsert);
        SqlClause nullableInsert = DML.insert(table)
                                      .values(cv(table.cStringNullable, "C_STRING_NULLABLE"));
        try {
            execInTx(nullableInsert);
            fail("NULL not allowed for column.");
        } catch (Exception e) {
            // pass
        }
    }

    @Test
    public void uniqueTest() {
        TabTUnique table = MODEL.table(TabTUnique.class);
        SqlClause uniqueInsert = DML.insert(table)
                                    .values(cv(table.cStringUnique, "STRING_ABC"),
                                            cv(table.cStringNotUnique, "STRING_XZY"));
        execInTx(uniqueInsert);
        SqlClause uniqueInsert2 = DML.insert(table)
                                     .values(cv(table.cStringUnique, "STRING_DEF"),
                                             cv(table.cStringNotUnique, "STRING_XZY"));
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
        TabTIdInsert table = MODEL.table(TabTIdInsert.class);
        SqlClause ins = DML.insert(table)
                           .values(cv(table.cId, 1L),
                                   cv(table.cStringValue, "C_STRING_VALUE"));
        long id = (long)execInTx(ins);
        assertEquals(1, id);
    }

    @Test
    public void idInsertImplicit() {
        TabTIdInsert table = MODEL.table(TabTIdInsert.class);
        SqlClause ins = DML.insert(table)
                           .values(cv(table.cStringValue, "C_STRING_VALUE"));
        long id = (long)execInTx(ins);
        assertEquals(1, id);
    }

    @Test
    public void insertValidReference() {
        TabTIdInsert tableId =MODEL.table(TabTIdInsert.class);
        TabTReferencing tableRef = MODEL.table(TabTReferencing.class);
        SqlClause ins = DML.insert(tableId)
                           .values(cv(tableId.cId, 1L),
                                   cv(tableId.cStringValue, "C_STRING_VALUE"));
        execInTx(ins);
        SqlClause insRef = DML.insert(tableRef)
                              .values(cv(tableRef.cFk, 1L));
        execInTx(insRef);
    }

    @Test
    public void insertInvalidReference() {
        TabTIdInsert tableId = MODEL.table(TabTIdInsert.class);
        TabTReferencing tableRef = MODEL.table(TabTReferencing.class);
        SqlClause ins = DML.insert(tableId)
                           .values(cv(tableId.cId, 1L),
                                   cv(tableId.cStringValue, "C_STRING_VALUE"));
        execInTx(ins);
        SqlClause insRef = DML.insert(tableRef)
                              .values(cv(tableRef.cFk, 2L));
        try {
            execInTx(insRef);
            fail("INVALID REFERENCE not allowed for column.");
        } catch (Exception e) {
            // pass
        }
    }
}
