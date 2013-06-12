package sk.jacob.sql;

public class CRUD {
    public static Select select(String ... columnNames) {
        return new Select(columnNames);
    }

    public static Statement insert() {
        return null;
    }

    public static Statement update() {
        return null;
    }

    public static Statement delete() {
        return null;
    }
}
