package sk.jacob.types;

import java.util.HashMap;
import java.util.Map;

public class SecurityType {
    public Principal principal;
    public final Map<String, Object> context = new HashMap<String, Object>();
}
