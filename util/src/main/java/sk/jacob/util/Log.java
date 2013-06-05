package sk.jacob.util;

public class Log {
    private static final String SOUT_PREFIX = "SYSOUT-> ";
    public static void sout(Object object) {
        System.out.println(SOUT_PREFIX + object);
    }
}
