package sk.jacob.sql.engine;

import sk.jacob.sql.dml.Delete;
import sk.jacob.sql.dml.Select;
import sk.jacob.sql.dml.Update;
import sk.jacob.sql.generator.IdGenerator;
import sk.jacob.sql.ddl.Column;
import sk.jacob.sql.ddl.ColumnValue;
import sk.jacob.sql.dialect.Statement;
import sk.jacob.sql.dml.Insert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static sk.jacob.sql.dml.DML.cv;

// UNDER CONSTRUCTION
// TO BE REVIEWED
public class ExecutionContext {
    private final DbEngine dbEngine;
    private final Connection connection;
    private final List<java.sql.Statement> sqlStatements = new ArrayList<>();

    ExecutionContext(DbEngine dbEngine) {
        this.dbEngine = dbEngine;
        this.connection = dbEngine.getConnection();
    }

    // FIXME
    public ResultSet execute(String stringStatement) {
        ResultSet resultSet;

        try {
            java.sql.Statement statement = this.connection.createStatement();
            sqlStatements.add(statement);
            resultSet = statement.executeQuery(stringStatement);
        } catch (Exception e) {
            this.close();
            throw new RuntimeException(e);
        }
        return resultSet;
    }

    public Object execute(Statement statement) {
        Statement rootStatement = statement.getRootStatement();
        Object returnValue = null;
        if(rootStatement instanceof Select) {
            returnValue = execute((Select)rootStatement);
        } else if(rootStatement instanceof Insert) {
            returnValue = execute((Insert)rootStatement);
        } else if(rootStatement instanceof Update) {
            execute((Update)rootStatement);
        } else if(rootStatement instanceof Delete) {
            execute((Delete)rootStatement);
        }
        return returnValue;
    }

    private ResultSet execute(Select statement) {
        return queryStatement(statement);
    }

    private Object execute(Insert statement) {
        return insertStatement(statement);
    }

    private void execute(Update statement) {
        updateDeleteStatement(statement);
    }

    private void execute(Delete statement) {
        updateDeleteStatement(statement);
    }

    private ResultSet queryStatement(Statement statement) {
        ResultSet resultSet;

        try {
            PreparedStatement ps = toPreparedStatement(statement);
            sqlStatements.add(ps);
            resultSet = ps.executeQuery();
        } catch (SQLException e) {
            this.close();
            throw new RuntimeException(e);
        }

        return resultSet;
    }

    private Object insertStatement(Statement statement) {
        Object generatedId = this.checkIdColumn((Insert)statement);

        try {
            PreparedStatement ps = toPreparedStatement(statement);
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

    private void updateDeleteStatement(Statement statement) {
        try {
            PreparedStatement ps = toPreparedStatement(statement);
            sqlStatements.add(ps);
            ps.executeUpdate();
        } catch (SQLException e) {
            this.close();
            throw new RuntimeException(e);
        }
    }

    private PreparedStatement toPreparedStatement(Statement statement) throws SQLException {
        Statement.CompiledStatement cs = statement.compile(dbEngine);
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
