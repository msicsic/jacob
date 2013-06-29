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

public abstract class DMLStatement implements SqlExpression{

    public static class ParamCounter {
        private Integer counter = new Integer(1);
        private Map<String, Object> bindParams = new HashMap<String, Object>();

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
    private DMLStatement parent;
    private CompiledStatement compiledStatement;

    protected DMLStatement() {
        this(null);
    }

    protected DMLStatement(DMLStatement parentDMLStatement) {
        this.parent = parentDMLStatement;
    }

    public void setParentStatement(DMLStatement parentDMLStatement) {
        this.parent = parentDMLStatement;
    }

    public DMLStatement getRootStatement() {
        return (this.parent == null) ? this
                                     : this.parent.getRootStatement();
    }

    public ParamCounter getParamCounter() {
        return getRootStatement().paramCounter;
    }

    public CompiledStatement compile() {
        return compile(new GenericDialectVisitor());
    }

    public CompiledStatement compile(DialectVisitor visitor) {
        if (this.getRootStatement().compiledStatement == null) {
            this.getRootStatement().compiledStatement =
                    new CompiledStatement(this.getRootStatement().sql(visitor),
                                          this.getRootStatement().paramCounter.parameters());
        }
        return this.getRootStatement().compiledStatement;
    }

    public CompiledStatement compile(DbEngine dbEngine) {
        return compile(dbEngine.getDialect());
    }
}
