package sk.jacob.appcommon;

import sk.jacob.appcommon.types.Interrupt;

public interface Raisable {
    String getCode();
    Interrupt.Type getType();
}
