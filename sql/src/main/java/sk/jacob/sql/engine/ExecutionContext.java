package sk.jacob.sql.engine;

import sk.jacob.sql.generator.IdGenerator;
import sk.jacob.sql.ddl.Column;
import sk.jacob.sql.ddl.ColumnValue;
import sk.jacob.sql.dialect.Statement;
import sk.jacob.sql.dml.Insert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import static sk.jacob.sql.dml.DML.cv;

// UNDER CONSTRUCTION
public class ExecutionContext {
    private final DbEngine dbEngine;
    private final Connection connection;

    ExecutionContext(DbEngine dbEngine) {
        this.dbEngine = dbEngine;
        this.connection = dbEngine.getConnection();
    }

    // FIXME
    public List<Long> execute(Statement statement) {
        List<Long> generatedIds = null;

        if(statement instanceof Insert) {
            this.checkIdColumn((Insert)statement);
        }

        Statement.CompiledStatement cs = statement.compile(dbEngine);
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(cs.normalizedStatement());
            this.dbEngine.bindParameters(preparedStatement, cs.parameterList());
            preparedStatement.executeUpdate();
            this.connection.commit();
        } catch (Exception e) {
            this.dbEngine.close(this.connection);
            throw new RuntimeException(e);
        }
        return generatedIds;
    }

    // FIXME
    public Long execute(String stringStatement) {
        Long id = null;
        try {
            java.sql.Statement statement = this.connection.createStatement();
            ResultSet result = statement.executeQuery(stringStatement);
            result.next();
            id = result.getLong(1);
        } catch (Exception e) {
            this.dbEngine.close(this.connection);
            throw new RuntimeException(e);
        }

        return id;
    }

    private void queryOperation() {

    }

    private void updateOperation() {

    }

    private void checkIdColumn(Insert insert) {
        if(insert.table == null)
            return;

        Column idColumn = insert.table.getIdColumn();
        if(idColumn == null)
            return;

        Boolean idColumnPresent = Boolean.FALSE;
        for(ColumnValue cv : insert.getColumnValues()) {
            idColumnPresent = cv.columnName.equalsIgnoreCase(idColumn.name);
        }

        if(idColumnPresent == Boolean.FALSE) {
            IdGenerator generator = idColumn.options.getGenerator();
            insert.addValue(cv(idColumn.name, generator.getIdValue(dbEngine)));
        }
    }

    public void txBegin(){}

    public void txCommit(){
        try {
            this.connection.commit();
        } catch (Exception e) {
            this.dbEngine.close(this.connection);
            throw new RuntimeException(e);
        }
    }

    public void txRollback(){
        try {
            this.connection.rollback();
        } catch (Exception e) {
            this.dbEngine.close(this.connection);
            throw new RuntimeException(e);
        }
    }

    public void close() {
        this.dbEngine.close(this.connection);
    }
}
