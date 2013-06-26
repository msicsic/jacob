package sk.jacob.sql.dialect;

import java.util.ArrayList;
import java.util.List;

public class DDLStatement {
    public final String inline;
    public final List<String> outline = new ArrayList<String>();

    public DDLStatement(String inline) {
        this(inline, null);
    }

    public DDLStatement(String inline, List<String> outline) {
        this.inline = inline;
        if(outline != null)
            this.outline.addAll(outline);
    }
}
