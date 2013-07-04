package sk.jacob.sql.ddl;

import sk.jacob.sql.dialect.DialectVisitor;
import sk.jacob.sql.generator.IdGenerator;

public class ColumnOptions implements DDLEpression {
    private Boolean primaryKey = Boolean.FALSE;
    private IdGenerator generator = null;
    private Column parentColumn;

    public ColumnOptions primaryKey() {
        return primaryKey(null);
    }
    public ColumnOptions primaryKey(IdGenerator generator) {
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
    public ColumnOptions nullable() {
        this.nullable = Boolean.TRUE;
        return this;
    }
    public Boolean isNullable() {
        return this.nullable;
    }


    private Boolean unique = Boolean.FALSE;
    public ColumnOptions unique() {
        this.unique = Boolean.TRUE;
        return this;
    }
    public Boolean isUnique() {
        return this.unique;
    }


    private ForeignKey foreignKey= null;
    public ColumnOptions foreignKey(String refTabCol) {
        this.foreignKey = new ForeignKey(refTabCol);
        return this;
    }
    public ColumnOptions foreignKey(String refTabCol, String constraintName) {
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
