package sk.jacob.common;

import sk.jacob.types.DataPacket;
import sk.jacob.types.Principal;
import sk.jacob.types.TokenType;

import java.util.Map;

public enum SECURITY {
    CONNECTION,
    PRINCIPAL,
    TOKEN;

    private static final String CONTEXT_KEY = "SECURITY_CONTEXT";

    public Object get(DataPacket dataPacket) {
        Map<String, Object> bc = dataPacket.context.get(CONTEXT_KEY);
        return bc.get(this.name());
    }

    public void set(DataPacket dataPacket, Object value) {
        Map<String, Object> bc = dataPacket.context.get(CONTEXT_KEY);
        bc.put(this.name(), value);
    }

    public static TokenType getToken(DataPacket dataPacket) {
        return (TokenType)SECURITY.TOKEN.get(dataPacket);
    }

    public static void setToken(DataPacket dataPacket, TokenType token) {
        SECURITY.PRINCIPAL.set(dataPacket, token);
    }

    public static Principal getPrincipal(DataPacket dataPacket) {
        return (Principal)SECURITY.PRINCIPAL.get(dataPacket);
    }

    public static void setPrincipal(DataPacket dataPacket, Principal principal) {
        SECURITY.PRINCIPAL.set(dataPacket, principal);
    }
}
