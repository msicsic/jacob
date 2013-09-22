package sk.jacob.appcommon.types;


import sk.jacob.appcommon.ApplicationException;
import sk.jacob.appcommon.Raisable;
import sk.jacob.common.util.locale.Bundle;

import java.util.Locale;

import static sk.jacob.common.util.StackTrace.stackToString;

public abstract class Interrupt {
    private static final Interrupt INTERRUPT = new Interrupt() {
        @Override
        public void raise(Raisable raisable) {
            throw new ApplicationException(INTERRUPT(raisable.getType(), raisable.getCode()));
        }
    };

    private static final Interrupt SILENT = new Interrupt() {
        @Override
        public void raise(Raisable raisable) {} //pass
    };

    private Interrupt(){}

    public static Interrupt ifFalse(boolean marker) {
        if (marker == false) return INTERRUPT;
        else return SILENT;
    }

    abstract public void raise(Raisable raisable);

    public static enum Type {
        ERROR,
        EXCEPTION;
    }

    private static Message INTERRUPT(Interrupt.Type it, String interuptCode) {
        return INTERRUPT(it, interuptCode, Bundle.getMessage(interuptCode, new Locale("SK")));
    }

    private static Message INTERRUPT(Interrupt.Type it, String interuptCode, Throwable throwable) {
        return INTERRUPT(it, interuptCode, stackToString(throwable));
    }

    private static Message INTERRUPT(Interrupt.Type it,  String interruptCode, String interruptText) {
        return generateInterruptMessage(it, interruptCode, interruptText);
    }

    private static Message generateInterruptMessage(Interrupt.Type it, String code, String text) {
        Message message = new Message();
        message.response.resh.status = MESSAGE_STATUS.INT.name();

        InterruptedResponseData interruptedResponseData = new InterruptedResponseData();
        message.response.resd = interruptedResponseData;
        interruptedResponseData.reason = it.name();
        interruptedResponseData.code = code;
        interruptedResponseData.text = text;
        return message;
    }
}
