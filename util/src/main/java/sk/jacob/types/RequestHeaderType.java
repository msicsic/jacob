package sk.jacob.types;

import sk.jacob.annotation.Required;

public class RequestHeaderType {
    public String messageId;
    @Required
    public String type;
    public String version;
    public transient TokenType token;
}
