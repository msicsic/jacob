package sk.jacob.sql.generator;

import sk.jacob.sql.ddl.Sequence;
import sk.jacob.sql.engine.DbEngine;

public class SequenceIdGenerator implements IdGenerator<Long> {
    private final Sequence sequence;

    private SequenceIdGenerator(Sequence sequence) {
        this.sequence = sequence;
    }

    @Override
    public Long getIdValue(DbEngine dbEngine) {
        return this.sequence.nextVal(dbEngine);
    }

    @Override
    public void equalize(DbEngine dbEngine, Long value) {
        while(this.sequence.nextVal(dbEngine) < value) {}
    }

    public static SequenceIdGenerator sequenceIdGenerator(Sequence sequence) {
        return new SequenceIdGenerator(sequence);
    }
}
