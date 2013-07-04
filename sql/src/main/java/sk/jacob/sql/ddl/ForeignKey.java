package sk.jacob.sql.ddl;

import sk.jacob.sql.dialect.DialectVisitor;

import java.util.Random;

public class ForeignKey extends DbObject {
    private static final Random RANDOM = new Random();
    public final String refTableName;
    public final String refColumnName;
    public final String constraintName;
    private Column parentColumn;

    public ForeignKey(String refTabCol) {
        this(refTabCol, generateConstraintName());
    }

    public ForeignKey(String refTabCol, String constraintName) {
        super(constraintName);
        String[] tabCol = refTabCol.split("\\.");
        this.refTableName = tabCol[0];
        this.refColumnName = tabCol[1];
        this.constraintName = constraintName;
    }

    private static String generateConstraintName() {
        float randomFloat = 100000 + RANDOM.nextFloat() * 900000;
        return String.valueOf( (int)randomFloat );
    }

    @Override
    public DDLStatement create(DialectVisitor visitor) {
        return visitor.create(this);
    }

    public Column getParentColumn() {
        return this.parentColumn;
    }

    public void setParentColumn(Column parentColumn) {
        this.parentColumn = parentColumn;
    }
}
