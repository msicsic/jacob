package sk.jacob.sql;

import sk.jacob.sql.dialect.DialectVisitor;

import java.util.Arrays;
import java.util.List;

public class Op {
    public static class And extends ConditionalOperation {
        public final List<ConditionalOperation> conditionalOperations;
        public And(ConditionalOperation ... conditionalOperations) {
            super();
            this.conditionalOperations = Arrays.asList(conditionalOperations);
        }

        @Override
        public String sql(DialectVisitor visitor) {
            return visitor.visit(this);
        }

        @Override
        public void setRootStatement(Statement rootStatement) {
            super.setRootStatement(rootStatement);
            for (ConditionalOperation co : this.conditionalOperations) {
                co.setRootStatement(rootStatement);
            }
        }
    }

    public static ConditionalOperation and(ConditionalOperation ... conditionalOperations) {
        return new And(conditionalOperations);
    }


    public static class Eq extends ConditionalOperation {
        public final String columnName;
        public final Object value;
        public Eq(String columnName, Object value) {
            this.columnName = columnName;
            this.value = value;
        }

        @Override
        public String sql(DialectVisitor visitor) {
            return visitor.visit(this);
        }
    }

    public static ConditionalOperation eq(String columnName, Object value) {
        return new Eq(columnName, value);
    }


    public static class Le extends ConditionalOperation {
        public final String columnName;
        public final Object value;
        public Le(String columnName, Object value) {
            this.columnName = columnName;
            this.value = value;
        }

        @Override
        public String sql(DialectVisitor visitor) {
            return visitor.visit(this);
        }
    }

    public static ConditionalOperation le(String columnName, Object value) {
        return new Le(columnName, value);
    }
}
