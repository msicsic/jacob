package sk.jacob.mpu.security.dbregistry.codes;

import sk.jacob.appcommon.Raisable;
import sk.jacob.appcommon.types.Interrupt;

public enum SecurityINT implements Raisable {
    INVALID_LOGIN_PASSWORD(Interrupt.Type.ERROR, "security.invalid.login.password"),
    INVALID_TOKEN(Interrupt.Type.EXCEPTION, "security.invalid.token");

    SecurityINT(Interrupt.Type type, String code) {
        this.code = code;
        this.type = type;
    }

    private final String code;
    private final Interrupt.Type type;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public Interrupt.Type getType() {
        return type;
    }
}
