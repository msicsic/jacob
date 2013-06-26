package sk.jacob.sql;

public class SequenceIdGenerator implements IdGenerator<Long> {
    private final Sequence sequence;

    private SequenceIdGenerator(Sequence sequence) {
        this.sequence = sequence;
    }

    @Override
    public Long getIdValue(DbEngine dbEngine) {
        return sequence.nextVal(dbEngine);
    }

    public static SequenceIdGenerator sequenceIdGenerator(Sequence sequence) {
        return new SequenceIdGenerator(sequence);
    }
}
