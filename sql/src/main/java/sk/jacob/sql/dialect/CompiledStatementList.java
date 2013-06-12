package sk.jacob.sql.dialect;

import sk.jacob.util.func.Functional;
import sk.jacob.util.func.StringReducer;

import java.util.ArrayList;

public class CompiledStatementList extends ArrayList<String>{
    public CompiledStatementList(String str) {
        super();
        super.add(str);
    }

    public CompiledStatementList(StringBuffer sb) {
        super();
        super.add(sb.toString());
    }

    public String toString() {
        return (String)Functional.reduce(StringReducer.instance(";\n"), this, null);
    }
}
