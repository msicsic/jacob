package sk.jacob.sql;

import sk.jacob.sql.dialect.DDLStatement;
import sk.jacob.sql.dialect.DialectVisitor;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Metadata{
    private final List<String> createOrder = new ArrayList<String>();
    private final Map<String, DbObject> dbObjects = new HashMap<String, DbObject>();

    public void add(DbObject dbObject) {
        createOrder.add(dbObject.name);
        dbObjects.put(dbObject.name, dbObject);
    }

    public void createAll(DbEngine engine) {
        List<DDLStatement> ddlStatements = ddlStatements(engine);
        Connection connection = engine.getConnection();
        try {
            Statement statement = connection.createStatement();
            for (DDLStatement ddl : ddlStatements) {
                statement.execute(ddl.inline);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DbEngine.close(connection);
        }
    }

    private List<DDLStatement> ddlStatements(DbEngine engine) {
        List<DDLStatement> ddlStatements = new ArrayList<DDLStatement>();
        DialectVisitor dialect = engine.getDialect();
        for(String objectName : createOrder) {
            DbObject dbObject = dbObjects.get(objectName);

            if(dbObject instanceof Table) {
                ddlStatements.add(dialect.visit((Table) dbObject));
            } else if (dbObject instanceof Sequence) {
                ddlStatements.add(dialect.visit((Sequence) dbObject));
            }
        }
        return ddlStatements;
    }
}
