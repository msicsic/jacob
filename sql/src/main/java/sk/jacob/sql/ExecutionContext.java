package sk.jacob.sql;

import sk.jacob.sql.dialect.Statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import static sk.jacob.sql.CRUD.cv;

// UNDER CONSTRUCTION
public class ExecutionContext {
    private final DbEngine dbEngine;

    ExecutionContext(DbEngine dbEngine) {
        this.dbEngine = dbEngine;
    }

    // FIXME
    public List<Long> execute(sk.jacob.sql.dialect.Statement statement) {
        List<Long> generatedIds = null;
        Connection connection = dbEngine.getConnection();
        if(statement instanceof Insert)
            this.checkIdColumn((Insert)statement);

        Statement.CompiledStatement cs = statement.compile(dbEngine);

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(cs.normalizedStatement());
            dbEngine.bindParameters(preparedStatement, cs.parameterList());
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            dbEngine.close(connection);
        }
        return generatedIds;
    }

    // FIXME
    public Long execute(String stringStatement) {
        Long id = null;
        Connection connection = dbEngine.getConnection();
        try {
            java.sql.Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(stringStatement);
            result.next();
            id = result.getLong(1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            dbEngine.close(connection);
        }

        return id;
    }

    private void checkIdColumn(Insert insert) {
        if(insert.table == null)
            return;

        Column idColumn = insert.table.getIdColumn();
        Boolean idColumnPresent = Boolean.FALSE;

        for(ColumnValue cv : insert.getColumnValues())
            idColumnPresent = cv.columnName.equalsIgnoreCase(idColumn.name);

        if(idColumnPresent == Boolean.FALSE) {
            IdGenerator generator = idColumn.options.getGenerator();
            insert.addValue(cv(idColumn.name, generator.getIdValue(dbEngine)));
        }
    }

    public void txBegin(){}
    public void txCommit(){}
    public void txRollback(){}
}
