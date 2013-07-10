package sk.jacob.sql.ddl;

import sk.jacob.sql.Metadata;
import sk.jacob.sql.dialect.DialectVisitor;
import sk.jacob.sql.engine.Connection;
import sk.jacob.sql.engine.DbEngine;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Sequence extends DbObject {
    public Sequence(String sequenceName, Metadata metadata) {
        super(sequenceName);
        metadata.add(this);
    }

    @Override
    public DDLStatement create(DialectVisitor visitor) {
        return visitor.create(this);
    }

    public Long nextVal(DbEngine dbEngine) {
        Long nextVal = null;
        DialectVisitor visitor = dbEngine.getDialect();
        String nextValStatement = visitor.sql(this);
        Connection conn = dbEngine.getConnection();
        ResultSet resultSet = conn.execute(nextValStatement);
        try {
            resultSet.next();
            nextVal = resultSet.getLong(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            conn.close();
        }
        return nextVal;
    }
}
