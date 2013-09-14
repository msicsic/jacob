package sk.jacob.sql.dml;

import sk.jacob.sql.dialect.DialectVisitor;
import sk.jacob.sql.dialect.GenericDialectVisitor;
import sk.jacob.sql.engine.DbEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class DMLClause implements SqlClause {

    public static class ParamCounter {
        private Integer counter = new Integer(1);
        private Map<String, Object> bindParams = new HashMap<>();

        public String addParam(String columnName, Object paramValue) {
            StringBuffer sb = new StringBuffer(":");
            sb.append(columnName);
            sb.append("_");
            sb.append(this.counter);
            sb.append(":");
            String paramName = sb.toString();
            this.bindParams.put(paramName, paramValue);
            this.counter++;
            return paramName;
        }

        public Map<String, Object> parameters() {
            return bindParams;
        }

        public void reset() {
            bindParams.clear();
        }
    }

    public static class CompiledStatement {
        private final String compiledStatement;
        private final Map<String, Object> parameters;
        private static final String PARAM_MATCHER_EXP = ":(.*?):";
        private static final Pattern PARAM_MATCHER = Pattern.compile(PARAM_MATCHER_EXP);

        private CompiledStatement(String compiledStatement, Map <String, Object> parameters) {
            this.compiledStatement = compiledStatement;
            this.parameters = parameters;
        }

        public String compiledStatement() {
            return this.compiledStatement;
        }

        public Map<String, Object> parameters() {
            return this.parameters;
        }

        private List<String> positionalParameters() {
            return new ArrayList<String>(){{
                Matcher match = PARAM_MATCHER.matcher(compiledStatement);
                while(match.find())
                    add(":"+match.group(1)+":");
            }};
        }

        public List<Object> parameterList() {
            List parameterList = new ArrayList();
            for(String parameterName : positionalParameters()) {
                parameterList.add(this.parameters.get(parameterName));
            }
            return parameterList;
        }

        public String normalizedStatement() {
            return compiledStatement.replaceAll(PARAM_MATCHER_EXP, "?");
        }
    }

    private final ParamCounter paramCounter = new ParamCounter();
    private DMLClause parent;
    private CompiledStatement compiledStatement;

    protected DMLClause() {
        this(null);
    }

    protected DMLClause(DMLClause parentDMLClause) {
        this.parent = parentDMLClause;
    }

    public void setParentStatement(DMLClause parentDMLClause) {
        this.parent = parentDMLClause;
    }

    public DMLClause getRootClause() {
        return (this.parent == null) ? this
                                     : this.parent.getRootClause();
    }

    public ParamCounter getParamCounter() {
        return getRootClause().paramCounter;
    }

    public CompiledStatement compile() {
        return compile(GenericDialectVisitor.INSTANCE);
    }

    public CompiledStatement compile(DbEngine dbEngine) {
        return compile(dbEngine.getDialect());
    }

    public CompiledStatement compile(DialectVisitor visitor) {
        DMLClause rootStatement = this.getRootClause();
        rootStatement.paramCounter.reset();
        rootStatement.compiledStatement = new CompiledStatement(this.getRootClause().sql(visitor),
                this.getRootClause().paramCounter.parameters());
        return rootStatement.compiledStatement;
    }

    public String dump() {
        CompiledStatement compiledStatement = this.compile();
        StringBuffer sb = new StringBuffer();
        sb.append("Compiled statement:\n");
        sb.append(compiledStatement.compiledStatement());
        sb.append("\n\nParameters:\n");
        sb.append(compiledStatement.parameters());
        sb.append("\n\nNormalized statement:\n");
        sb.append(compiledStatement.normalizedStatement());
        sb.append("\n\nPositional parameters:\n");
        sb.append(compiledStatement.parameterList());
        sb.append("\n");
        return sb.toString();
    }

    @Override
    public String toString() {
        CompiledStatement compiledStatement = this.compile();
        return compiledStatement.compiledStatement();
    }
}
