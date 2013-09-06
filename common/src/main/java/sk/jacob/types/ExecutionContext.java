package sk.jacob.types;

import java.util.HashMap;
import java.util.Map;

public class ExecutionContext {
    public EXECUTION_CONTEXT status = EXECUTION_CONTEXT.AFP;
    public final Map<String, Map<String, Object>> INSTANCE = new HashMap<>();
}
