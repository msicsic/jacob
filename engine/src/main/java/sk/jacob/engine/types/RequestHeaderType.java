package sk.jacob.engine.types;

import sk.jacob.engine.handler.Required;

public class RequestHeaderType {
    public String messageId;

    @Required
    public String type;
}
