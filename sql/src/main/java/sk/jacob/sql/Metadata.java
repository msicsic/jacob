package sk.jacob.sql;

import sk.jacob.sql.ddl.DDLStatement;
import sk.jacob.sql.ddl.DbObject;
import sk.jacob.sql.ddl.Sequence;
import sk.jacob.sql.ddl.Table;
import sk.jacob.sql.engine.Connection;
import sk.jacob.sql.engine.DbEngine;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Metadata{
    private final List<String> createOrder = new ArrayList<>();
    private final Map<String, DbObject> dbObjects = new ConcurrentHashMap<>();

    public void add(DbObject dbObject) {
        // TODO: Implement order creation logic
        // Metadata must allow add objects not constrained by foreign keys.
        if (dbObjects.containsKey(dbObject.name)) {
            return;
        }
        createOrder.add(dbObject.name);
        dbObjects.put(dbObject.name, dbObject);
    }

    public void createAll(DbEngine dbEngine) {
        for(String objectName : createOrder) {
            create(dbObjects.get(objectName), dbEngine);
        }
    }

    public void create(DbObject dbObject, DbEngine dbEngine) {
        Connection conn = dbEngine.getConnection();
        try {
            DDLStatement ddl = dbObject.create(dbEngine);
            conn.execute(ddl);
        } finally {
            conn.close();
        }
    }

    public void dropAll(DbEngine dbEngine) {
        List<String> dropOrder = new ArrayList<>(createOrder);
        Collections.reverse(dropOrder);
        for(String objectName : dropOrder) {
            drop(dbObjects.get(objectName), dbEngine);
        }
    }

    public void drop(DbObject dbObject, DbEngine dbEngine) {
        Connection conn = dbEngine.getConnection();
        try {
            DDLStatement ddl = dbObject.drop(dbEngine);
            conn.execute(ddl);
        } finally {
            conn.close();
        }
    }

    public <T> T table(Class<T> name) {
        return (T)table(getTableName(name));
    }

    public Table table(String tableName) {
        Object table = dbObjects.get(tableName);
        if (table == null) {
            throw new NullPointerException("Table " + tableName + " is not defined");
        }
        return (Table)table;
    }

    public Sequence sequence(String sequenceName) {
        Object sequence = dbObjects.get(sequenceName);
        if (sequence == null) {
            throw new NullPointerException("Sequence " + sequenceName + " is not defined");
        }
        return (Sequence)sequence;
    }

    public boolean isDefined(String objectName) {
        return dbObjects.containsKey(objectName);
    }

    private static String getTableName(Class<?> cls) {
        String tableName;
        try {
            Field f;
            f = cls.getField("NAME");
            tableName = (String)f.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return tableName;
    }
}
