package sk.jacob.sql.ddl;

import sk.jacob.sql.dialect.DialectVisitor;
import sk.jacob.sql.generator.IdGenerator;

public class ColumnOptions implements DDLEpression, IColumnOptions {
    private Boolean primaryKey = Boolean.FALSE;
    private IdGenerator generator = null;
    private Column parentColumn;

    @Override
    public IColumnOptions primaryKey() {
        return primaryKey(null);
    }
    @Override
    public IColumnOptions primaryKey(IdGenerator generator) {
        this.generator = generator;
        this.primaryKey = Boolean.TRUE;
        return this;
    }
    public Boolean isPrimaryKey() {
        return this.primaryKey;
    }
    public IdGenerator getGenerator() {
        return this.generator;
    }


    private Boolean nullable = Boolean.FALSE;
    @Override
    public IColumnOptions nullable(Boolean nullable) {
        this.nullable = nullable;
        return this;
    }
    public Boolean isNullable() {
        return this.nullable;
    }


    private Boolean unique = Boolean.FALSE;
    @Override
    public IColumnOptions unique(Boolean unique) {
        this.unique = unique;
        return this;
    }
    public Boolean isUnique() {
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
