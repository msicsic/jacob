package sk.jacob.appcommon.accessor;

import sk.jacob.appcommon.types.Principal;
import sk.jacob.appcommon.types.Token;
import sk.jacob.sql.engine.Connection;

public class SECURITY {
    public static final String CONNECTION_KEY = "/RESOURCES/SECURITY/CONNECTION";
    public static final ECAcessor<Principal> PRINCIPAL = new ECAcessor<>("/RESOURCES/SECURITY/PRINCIPAL");
    public static final ECAcessor<Token> TOKEN = new ECAcessor<>("/RESOURCES/SECURITY/TOKEN");
    public static final ECAcessor<Connection> CONNECTION = new ECAcessor<>(CONNECTION_KEY);
}