package sk.jacob.types;

import sk.jacob.annotation.Required;

public class RequestHeader {
    public String messageId;
    @Required
    public String type;
    public String version;
    public Token token;
}
