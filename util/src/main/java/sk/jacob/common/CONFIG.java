package sk.jacob.common;

import java.util.Properties;

public enum CONFIG {
    ADMIN_LOGIN("admin.login"),
    ADMIN_PASSWORD("admin.md5pwd"),

    CONTEXT_URL("context.url"),
    CONTEXT_USERNAME("context.username"),
    CONTEXT_PASSWORD("context.password"),

    SECURITY_URL("security.url"),
    SECURITY_USERNAME("security.username"),
    SECURITY_PASSWORD("security.password"),

    LDS_BDS_URL("lds_bds.url"),
    LDS_BDS_USERNAME("lds_bds.username"),
    LDS_BDS_PASSWORD("lds_bds.password");

    CONFIG(String key) {
        this.key = key;
    }

    private final String key;

    public String get(Properties config) {
        return config.getProperty(this.key);
    }
}

