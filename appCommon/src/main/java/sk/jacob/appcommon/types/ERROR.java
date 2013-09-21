package sk.jacob.appcommon.types;

public abstract class ERROR {
    private static ERROR READY = new ERROR() {
        @Override
        public void raise(String errorCode) {
            throw new RuntimeException(errorCode);
        }
    };

    private static ERROR SILENT = new ERROR() {
        @Override
        public void raise(String errorCode) {}
    };

    private ERROR(){}

    public static ERROR ifFalse(boolean isOK) {
        if (isOK) return SILENT;
        else return READY;
    }

    abstract public void raise(String errorCode);
}
