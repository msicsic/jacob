package sk.jacob.sql;

import sk.jacob.sql.dialect.DialectVisitor;
import sk.jacob.sql.dialect.GenericDialectVisitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Statement {

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

        public List<String> positionalParameters() {
            return new ArrayList<String>(){{
                Matcher match = PARAM_MATCHER.matcher(compiledStatement);
                while(match.find())
                    add(":"+match.group(1)+":");
            }};
        }

        public String normalizedStatement() {
            return compiledStatement.replaceAll(PARAM_MATCHER_EXP, "?");
        }
    }

    private final ParamCounter paramCounter = new ParamCounter();
    private Statement parent;

    protected Statement() {
        this(null);
    }

    protected Statement(Statement parentStatement) {
        this.parent = parentStatement;
    }

    public void setParentStatement(Statement parentStatement) {
        this.parent = parentStatement;
    }

    public Statement getRootStatement() {
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
        return new CompiledStatement(this.getRootStatement().sql(visitor),
                                     this.getRootStatement().paramCounter.parameters());
    }

    public CompiledStatement compile(DbEngine dbEngine) {
        throw new UnsupportedOperationException();
    }

    public abstract String sql(DialectVisitor visitor);
}
