package sk.jacob.sql;

import org.junit.After;
import org.junit.Before;
import sk.jacob.sql.model.TestModel;
import sk.jacob.sql.dml.SqlClause;
import sk.jacob.sql.engine.Connection;
import sk.jacob.sql.engine.DbEngine;

public class BaseTest {
    protected final Metadata MODEL = TestModel.METADATA;
    protected final DbEngine engine = new DbEngine("jdbc:h2:/jacobTest/testModel", "sa", "sa");
    protected Connection connection;

    @Before
    public void setup() {
        MODEL.createAll(engine);
        connection = engine.getConnection();
        connection.bindMetadata(MODEL);
    }

    @After
    public void tearDown() {
        connection.close();
        connection = null;
        MODEL.dropAll(engine);
    }

    protected Object execInTx(SqlClause sqlClause) {
        connection.txBegin();
        Object returnValue = connection.execute(sqlClause);
        connection.txCommit();
        return returnValue;
    }
}
