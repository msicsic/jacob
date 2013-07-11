package sk.jacob.sql.engine;

import sk.jacob.sql.Metadata;
import sk.jacob.sql.ddl.DDLStatement;
import sk.jacob.sql.ddl.Table;
import sk.jacob.sql.dml.*;
import sk.jacob.sql.generator.IdGenerator;
import sk.jacob.sql.ddl.Column;
import sk.jacob.sql.ddl.ColumnValue;
import sk.jacob.sql.dml.DMLClause;

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
    private Metadata metadata;

    Connection(DbEngine dbEngine) {
        this.dbEngine = dbEngine;
        this.connection = dbEngine.dbConnect();
    }

    public void bindMetadata(Metadata metadata) {
        this.metadata = metadata;
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

    public Object execute(SqlClause sqlClause) {
        return execute((DMLClause) sqlClause);
    }

    public Object execute(DMLClause dmlClause) {
        DMLClause rootDMLClause = dmlClause.getRootClause();
        Object returnValue = null;
        if(rootDMLClause instanceof Select) {
            returnValue = executeStatement((Select) rootDMLClause);
        } else if(rootDMLClause instanceof Insert) {
            returnValue = executeStatement((Insert) rootDMLClause);
        } else if(rootDMLClause instanceof Update) {
            executeStatement((Update) rootDMLClause);
        } else if(rootDMLClause instanceof Delete) {
            executeStatement((Delete) rootDMLClause);
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
        Object generatedId = checkIdColumn(dmlStatement);
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

        if(this.metadata == null && insert.table == null) {
            throw new NullPointerException("Prior using plain SQL INSERT construction, metadata must be binded " +
                    "to connection. Call bindMetadata(metadataInstance) on connection before insert. " +
                    "Metadata is used to generate id for id column.");
        }

        Table table = (insert.table == null) ? this.metadata.table(insert.tableName)
                                             : insert.table;

        Column idColumn = table.getIdColumn();
        if(idColumn == null) {
            return null;
        }

        ColumnValue idValue = idValue(insert.getColumnValues(), idColumn);
        if(idValue == null) {
            IdGenerator generator = idColumn.options.getGenerator();
            generatedId = generator.getIdValue(dbEngine);
            insert.addValue(cv(idColumn.name, generatedId));
        } else {
            generatedId = idValue.value;
            IdGenerator generator = idColumn.options.getGenerator();
            generator.equalize(dbEngine, idValue.value);
        }

        return generatedId;
    }

    private ColumnValue idValue(List<ColumnValue> filledColumns, Column tableIdColumn) {
        ColumnValue filledIdColumn = null;
        for(ColumnValue cv : filledColumns) {
            if ( cv.columnName.equalsIgnoreCase(tableIdColumn.name) ) {
                filledIdColumn = cv;
                break;
            }
        }
        return filledIdColumn;
    }

    private void executeStatement(Update dmlStatement) {
        executeUpdateOrDelete(dmlStatement);
    }

    private void executeStatement(Delete dmlStatement) {
        executeUpdateOrDelete(dmlStatement);
    }

    private void executeUpdateOrDelete(DMLClause dmlClause) {
        PreparedStatement ps = null;
        try {
            ps = toPreparedStatement(dmlClause);
            ps.executeUpdate();
        } catch (SQLException e) {
            this.close();
            throw new RuntimeException(e);
        } finally {
            DbEngine.close(ps);
        }
    }

    private PreparedStatement toPreparedStatement(DMLClause dmlClause) throws SQLException {
        DMLClause.CompiledStatement cs = dmlClause.compile(this.dbEngine);
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
