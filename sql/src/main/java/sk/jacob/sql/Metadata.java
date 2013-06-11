package sk.jacob.sql;

import sk.jacob.sql.dialect.CompiledStatementList;
import sk.jacob.sql.dialect.DialectVisitor;

import java.util.ArrayList;
import java.util.List;

import java.sql.*;

public class Metadata{
    public List<DbObject> tables = new ArrayList<DbObject>();
    public void add(DbObject dbObject) {
        tables.add(dbObject);
    }

    public void createAll(DbEngine engine) {
        Connection connection = engine.getConnection();
        //String sql = ddlStatements(engine.getDialect()).toString();
        try {
            java.sql.Statement statement = connection.createStatement();
            //statement.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            //engine.closeConnection(connection);
        }
    }

    private CompiledStatementList ddlStatements(DialectVisitor dialect) {
        CompiledStatementList sql = null;
        for(DbObject object : tables) {
            sql = object.sql(dialect);
        }
        return sql;
    }
}
