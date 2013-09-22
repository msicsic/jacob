package sk.jacob.appcommon;

import sk.jacob.appcommon.types.Message;

public class ApplicationException extends RuntimeException {
    public final Message message;

    public ApplicationException(Raisable raisable) {
        this.message = null;
    }
}
