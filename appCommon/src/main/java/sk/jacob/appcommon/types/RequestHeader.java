package sk.jacob.appcommon.types;

import sk.jacob.appcommon.annotation.Required;

public class RequestHeader {
    public String messageId;
    @Required
    public String type;
    public String version;
    public Token token;
}
