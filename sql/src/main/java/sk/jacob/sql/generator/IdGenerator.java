package sk.jacob.sql.generator;

import sk.jacob.sql.engine.DbEngine;

public interface IdGenerator<T> {
    T getIdValue(DbEngine dbEngine);
}
