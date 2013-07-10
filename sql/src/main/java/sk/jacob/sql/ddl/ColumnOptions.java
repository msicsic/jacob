package sk.jacob.sql.ddl;

import sk.jacob.sql.dialect.DialectVisitor;
import sk.jacob.sql.generator.IdGenerator;

public class ColumnOptions implements DDLEpression, IColumnOptions {
    private boolean primaryKey = false;
    private IdGenerator generator = null;
    private Column parentColumn;

    @Override
    public IColumnOptions primaryKey() {
        return primaryKey(null);
    }
    @Override
    public IColumnOptions primaryKey(IdGenerator generator) {
        this.generator = generator;
        this.primaryKey = true;
        return this;
    }
    public boolean isPrimaryKey() {
        return this.primaryKey;
    }
    public IdGenerator getGenerator() {
        return this.generator;
    }


    private boolean nullable = false;
    @Override
    public IColumnOptions nullable(boolean nullable) {
        this.nullable = nullable;
        return this;
    }
    public boolean isNullable() {
        return this.nullable;
    }


    private boolean unique = false;
    @Override
    public IColumnOptions unique(boolean unique) {
        this.unique = unique;
        return this;
    }
    public boolean isUnique() {
        return this.unique;
    }


    private ForeignKey foreignKey= null;
    @Override
    public IColumnOptions foreignKey(String refTabCol) {
        this.foreignKey = new ForeignKey(refTabCol);
        return this;
    }
    @Override
    public IColumnOptions foreignKey(String refTabCol, String constraintName) {
        this.foreignKey = new ForeignKey(refTabCol, constraintName);
        return this;
    }
    public ForeignKey getForeignKey() {
        return this.foreignKey;
    }

    @Override
    public DDLStatement create(DialectVisitor dialect) {
        return dialect.create(this);
    }

    @Override
    public DDLStatement drop(DialectVisitor visitor) {
        throw new UnsupportedOperationException();
    }

    public Column getParentColumn() {
        return this.parentColumn;
    }

    public void setParentColumn(Column parentColumn) {
        this.parentColumn = parentColumn;
        if(this.foreignKey != null) {
            this.foreignKey.setParentColumn(parentColumn);
        }
    }
}
