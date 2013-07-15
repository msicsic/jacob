package sk.jacob.sql.dml;

import sk.jacob.sql.ddl.Column;
import sk.jacob.sql.dialect.DialectVisitor;
import sk.jacob.sql.dialect.GenericDialectVisitor;

import java.util.Arrays;
import java.util.Collection;
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
            return visitor.sql(this);
        }

        @Override
        public void setParentStatement(DMLClause parentDMLClause) {
            super.setParentStatement(parentDMLClause);
            for (ConditionalOperation co : this.conditionalOperations) {
                co.setParentStatement(this);
            }
        }
    }

    public static ConditionalOperation and(ConditionalOperation ... conditionalOperations) {
        return new And(conditionalOperations);
    }


    public static class Eq extends ConditionalOperation {
        public final Object column;
        public final Object value;

        public Eq(String columnName, Object value) {
            this.column = columnName;
            this.value = value;
        }

        public Eq(Column column, Object value) {
            this.column = column;
            this.value = value;
        }

        @Override
        public String sql(DialectVisitor visitor) {
            return visitor.sql(this);
        }
    }

    public static ConditionalOperation eq(String columnName, Object value) {
        return new Eq(columnName, value);
    }

    public static ConditionalOperation eq(Column column, Object value) {
        return new Eq(column, value);
    }

    public static class Le extends ConditionalOperation {
        public final Object column;
        public final Object value;

        public Le(String columnName, Object value) {
            this.column = columnName;
            this.value = value;
        }

        public Le(Column column, Object value) {
            this.column = column;
            this.value = value;
        }

        @Override
        public String sql(DialectVisitor visitor) {
            return visitor.sql(this);
        }
    }

    public static ConditionalOperation le(String columnName, Object value) {
        return new Le(columnName, value);
    }
    
    public static class In extends ConditionalOperation {
        public final Object column;
        public final Collection<? extends Object> values;

        public In(String columnName, Collection<? extends Object> values) {
            this.column = columnName;
            this.values = values;
        }

        public In(Column column, Collection<? extends Object> values) {
            this.column = column;
            this.values = values;
        }

        @Override
        public String sql(DialectVisitor visitor) {
            return visitor.sql(this);
        }
    }
        
    public static ConditionalOperation in(String columnName, Collection<? extends Object> values) {
        return new In(columnName, values);
    }

    public static ConditionalOperation in(Column column, Collection<? extends Object> values) {
        return new In(column, values);
    }
}
