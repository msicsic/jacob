package sk.jacob.mpu.security.dbregistry.codes;

import sk.jacob.appcommon.Raisable;

public enum SecurityINT implements Raisable {
    INVALID_LOGIN_PASSWORD("security.invalid.login.password"),
    INVALID_TOKEN("security.invalid.token");

    SecurityINT(String code) {
        this.code = code;
    }

    private final String code;

    @Override
    public String getCode() {
        return code;
    }
}
