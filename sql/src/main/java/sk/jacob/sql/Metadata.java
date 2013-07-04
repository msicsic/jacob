package sk.jacob.sql;

import sk.jacob.sql.ddl.DDLStatement;
import sk.jacob.sql.ddl.DbObject;
import sk.jacob.sql.engine.DbEngine;
import sk.jacob.sql.engine.ExecutionContext;

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
        // TODO: Implement order creation logic
        createOrder.add(dbObject.name);
        dbObjects.put(dbObject.name, dbObject);
    }

    public void createAll(DbEngine dbEngine) {
        for(String objectName : createOrder) {
            DbObject dbObject = dbObjects.get(objectName);
            ExecutionContext ectx = dbEngine.getExecutionContext();
            try {
                DDLStatement ddl = dbObject.create(dbEngine);
                ectx.execute(ddl);
            } finally {
                ectx.close();
            }
        }
    }
}
