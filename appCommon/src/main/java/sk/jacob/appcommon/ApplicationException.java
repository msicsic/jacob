package sk.jacob.appcommon;

import sk.jacob.appcommon.types.Message;

public class ApplicationException extends RuntimeException {
    public final Message message;

    public ApplicationException(Message message) {
        this.message = null;
    }
}
