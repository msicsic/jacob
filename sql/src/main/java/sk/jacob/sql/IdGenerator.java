package sk.jacob.sql;

public interface IdGenerator<T> {
    T getIdValue(DbEngine dbEngine);
}
