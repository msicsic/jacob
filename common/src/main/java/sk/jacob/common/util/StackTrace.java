package sk.jacob.common.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class StackTrace {
    public static String stackToString(Throwable t) {
        StringWriter stringWriter = new StringWriter();
        t.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }
}
