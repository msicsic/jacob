package sk.jacob.types;

import java.util.HashMap;
import java.util.Map;

public class DataPacket {
    public DATAPACKET_STATUS status = DATAPACKET_STATUS.AFP;
    public final Map<String, Map<String, Object>> CONTEXT = new HashMap<>();
}
