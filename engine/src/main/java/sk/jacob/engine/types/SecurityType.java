package sk.jacob.engine.types;

import java.util.HashMap;
import java.util.Map;

public class SecurityType {
    public String username;
    public TokenType token;
    public final Map<String, Object> context = new HashMap<String, Object>();
}
