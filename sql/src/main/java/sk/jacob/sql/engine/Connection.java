package sk.jacob.sql.engine;

import sk.jacob.sql.ddl.DDLStatement;
import sk.jacob.sql.dml.*;
import sk.jacob.sql.generator.IdGenerator;
import sk.jacob.sql.ddl.Column;
import sk.jacob.sql.ddl.ColumnValue;
import sk.jacob.sql.dml.DMLStatement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static sk.jacob.sql.dml.DML.cv;

// UNDER CONSTRUCTION
// TO BE REVIEWED
public class Connection {
    private final DbEngine dbEngine;
    private final java.sql.Connection connection;
    private final List<java.sql.Statement> sqlStatements = new ArrayList<>();

    Connection(DbEngine dbEngine) {
        this.dbEngine = dbEngine;
        this.connection = dbEngine.dbConnect();
    }

    public ResultSet execute(String stringStatement) {
        ResultSet resultSet;
        try {
            java.sql.Statement statement = this.connection.createStatement();
            sqlStatements.add(statement);
            resultSet = statement.executeQuery(stringStatement);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return resultSet;
    }

    public Object execute(DMLStatement dmlStatement) {
        DMLStatement rootDMLStatement = dmlStatement.getRootStatement();
        Object returnValue = null;
        if(rootDMLStatement instanceof Select) {
            returnValue = this.executeStatement((Select) rootDMLStatement);
        } else if(rootDMLStatement instanceof Insert) {
            returnValue = this.executeStatement((Insert) rootDMLStatement);
        } else if(rootDMLStatement instanceof Update) {
            this.executeStatement((Update) rootDMLStatement);
        } else if(rootDMLStatement instanceof Delete) {
            this.executeStatement((Delete) rootDMLStatement);
        }
        return returnValue;
    }

    public void execute(DDLStatement ddl) {
        executeDDL(ddl.inline);
        for(String outline : ddl.outline) {
            executeDDL(outline);
        }
    }

    private void executeDDL(String ddl) {
        java.sql.Statement statement = null;
        try {
            statement = this.connection.createStatement();
            statement.execute(ddl);
        } catch (SQLException e) {
            DbEngine.close(statement);
            throw new RuntimeException(e);
        }
    }

    private ResultSet executeStatement(Select dmlStatement) {
        ResultSet resultSet;
        try {
            PreparedStatement ps = toPreparedStatement(dmlStatement);
            sqlStatements.add(ps);
            resultSet = ps.executeQuery();
        } catch (SQLException e) {
            this.close();
            throw new RuntimeException(e);
        }
        return resultSet;
    }

    private Object executeStatement(Insert dmlStatement) {
        Object generatedId = this.checkIdColumn(dmlStatement);
        try {
            PreparedStatement ps = toPreparedStatement(dmlStatement);
            sqlStatements.add(ps);
            ps.executeUpdate();
        } catch (SQLException e) {
            this.close();
            throw new RuntimeException(e);
        }
        return generatedId;
    }

    private Object checkIdColumn(Insert insert) {
        Object generatedId = null;
        if(insert.table == null) { return null; }

        Column idColumn = insert.table.getIdColumn();
        if(idColumn == null) { return null; }

        Boolean idColumnPresent = Boolean.FALSE;
        for(ColumnValue cv : insert.getColumnValues()) {
            idColumnPresent = cv.columnName.equalsIgnoreCase(idColumn.name);
        }

        if(idColumnPresent == Boolean.FALSE) {
            IdGenerator generator = idColumn.options.getGenerator();
            generatedId = generator.getIdValue(dbEngine);
            insert.addValue(cv(idColumn.name, generatedId));
        }

        return generatedId;
    }

    private void executeStatement(Update dmlStatement) {
        this.executeUpdateOrDelete(dmlStatement);
    }

    private void executeStatement(Delete dmlStatement) {
        this.executeUpdateOrDelete(dmlStatement);
    }

    private void executeUpdateOrDelete(DMLStatement dmlStatement) {
        PreparedStatement ps = null;
        try {
            ps = toPreparedStatement(dmlStatement);
            ps.executeUpdate();
        } catch (SQLException e) {
            this.close();
            throw new RuntimeException(e);
        } finally {
            DbEngine.close(ps);
        }
    }

    private PreparedStatement toPreparedStatement(DMLStatement dmlStatement) throws SQLException {
        DMLStatement.CompiledStatement cs = dmlStatement.compile(this.dbEngine);
        PreparedStatement  preparedStatement = this.connection.prepareStatement(cs.normalizedStatement());
        DbEngine.bindParameters(preparedStatement, cs.parameterList());
        return preparedStatement;
    }

    public void txBegin(){}

    public void txCommit(){
        try {
            this.connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void txRollback(){
        try {
            this.connection.rollback();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        for(java.sql.Statement statement : this.sqlStatements) {
            DbEngine.close(statement);
        }
        this.sqlStatements.clear();
        DbEngine.close(this.connection);
    }
}
