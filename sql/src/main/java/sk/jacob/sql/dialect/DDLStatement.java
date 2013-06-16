package sk.jacob.sql.dialect;

import sk.jacob.util.func.Functional;
import sk.jacob.util.func.StringReducer;

import java.util.ArrayList;
import java.util.List;

public class DDLStatement {
    public final String inline;
    public final List<String> outline = new ArrayList<String>();

    public DDLStatement(String inline, List<String> outline) {
        this.inline = inline;
        if(outline != null)
            this.outline.addAll(outline);
    }
}
