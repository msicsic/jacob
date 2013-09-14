package sk.jacob.appcommon.accessor;

import sk.jacob.sql.engine.Connection;

public class CONTEXT {
    public static ECAcessor<String> LDS_BDS = new ECAcessor<>("/RESOURCES/BUSSINESS/LDS/BDS");
    public static ECAcessor<Connection> CONNECTION = new ECAcessor<>("/RESOURCES/CONTEXT/CONNECTION");
}