package sk.jacob.appcommon.types;


import sk.jacob.appcommon.ApplicationException;

public abstract class ERROR {
    private static ERROR ERROR = new ERROR() {
        @Override
        public void raise(String errorCode) {
            throw new ApplicationException(null);
        }
    };

    private static ERROR SILENT = new ERROR() {
        @Override
        public void raise(String errorCode) {} //pass
    };

    private ERROR(){}

    public static ERROR ifFalse(boolean isError) {
        if (!isError) return ERROR;
        else return SILENT;
    }

    abstract public void raise(String errorCode);
}
