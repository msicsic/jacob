package sk.jacob.common;

import sk.jacob.types.DataPacket;
import sk.jacob.types.Principal;
import sk.jacob.types.TokenType;

public enum SECURITY {
    EXECUTION_CTX;
    public Object get(DataPacket dataPacket) {
        return dataPacket.security.context.get(this.name());
    }
    public void set(DataPacket dataPacket, Object value) {
        dataPacket.security.context.put(this.name(), value);
    }
    public static TokenType getToken(DataPacket dataPacket) {
        return dataPacket.message.request.reqh.token;
    }
    public static void setToken(DataPacket dataPacket, TokenType token) {
        dataPacket.message.request.reqh.token = token;
    }
    public static Principal getPrincipal(DataPacket dataPacket) {
        return dataPacket.security.principal;
    }
    public static void setPrincipal(DataPacket dataPacket, Principal principal) {
        dataPacket.security.principal = principal;
    }
}
