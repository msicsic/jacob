package sk.jacob.sql;

import sk.jacob.sql.dialect.DDLStatement;
import sk.jacob.sql.dialect.DialectVisitor;
import sk.jacob.sql.dialect.GenericDialectVisitor;

import java.io.Closeable;
import java.sql.*;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

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
                try {
                statement.execute(ddl.inline);
                } catch (Exception e) {
                    // pass
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(connection);
        }
    }

    private void close(Connection connection) {
        try {
            connection.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<DDLStatement> ddlStatements(DbEngine engine) {
        List<DDLStatement> ddlstatemens = new ArrayList<DDLStatement>();
        for(String objectName : createOrder) {
            DbObject dbObject = dbObjects.get(objectName);
            if(isTable(dbObject)) {
                DialectVisitor dialect = engine.getDialect();
                DDLStatement ddlStatement = dialect.visit((Table) dbObject);
                ddlstatemens.add(ddlStatement);
            }
        }
        return ddlstatemens;
    }

    private Boolean isTable(DbObject dbObject){
        return dbObject instanceof Table;
    }
}
