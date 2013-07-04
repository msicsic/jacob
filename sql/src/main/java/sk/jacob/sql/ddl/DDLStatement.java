package sk.jacob.sql.ddl;

import java.util.ArrayList;
import java.util.List;

public class DDLStatement {
    public final String inline;
    public final List<String> outline = new ArrayList<String>();

    public DDLStatement(String inline) {
        this.inline = inline;
    }

    public DDLStatement(String inline, List<String> outline) {
        this.inline = inline;
        this.outline.addAll(outline);
    }
}
