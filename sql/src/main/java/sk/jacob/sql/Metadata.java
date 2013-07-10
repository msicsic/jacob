package sk.jacob.sql;

import sk.jacob.sql.ddl.DDLStatement;
import sk.jacob.sql.ddl.DbObject;
import sk.jacob.sql.ddl.Table;
import sk.jacob.sql.engine.Connection;
import sk.jacob.sql.engine.DbEngine;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Metadata{
    private final List<String> createOrder = new ArrayList<String>();
    private final Map<String, DbObject> dbObjects = new ConcurrentHashMap<>();

    public void add(DbObject dbObject) {
        // TODO: Implement order creation logic
        createOrder.add(dbObject.name);
        dbObjects.put(dbObject.name, dbObject);
    }

    public void createAll(DbEngine dbEngine) {
        for(String objectName : createOrder) {
            DbObject dbObject = dbObjects.get(objectName);
            Connection conn = dbEngine.getConnection();
            try {
                DDLStatement ddl = dbObject.create(dbEngine);
                conn.execute(ddl);
            } finally {
                conn.close();
            }
        }
    }

    public <T> T table(Class<T> name) {
        return (T)dbObjects.get(getTableName(name));
    }

    private static String getTableName(Class<?> cls) {
        String tableName = null;
        try {
            Field f = null;
            f = cls.getField("NAME");
            tableName = (String)f.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return tableName;
    }
}
