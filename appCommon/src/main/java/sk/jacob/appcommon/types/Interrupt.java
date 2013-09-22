package sk.jacob.appcommon.types;


import sk.jacob.appcommon.ApplicationException;
import sk.jacob.appcommon.Raisable;

public abstract class Interrupt {
    private static Interrupt interrupt = new Interrupt() {
        @Override
        public void raise(Raisable raisable) {
            throw new ApplicationException(raisable);
        }
    };

    private static Interrupt SILENT = new Interrupt() {
        @Override
        public void raise(Raisable raisable) {} //pass
    };

    private Interrupt(){}

    public static Interrupt ifFalse(boolean marker) {
        if (marker == false) return interrupt;
        else return SILENT;
    }

    abstract public void raise(Raisable raisable);

    public static enum Type {
        ERROR,
        EXCEPTION;
    }
}
