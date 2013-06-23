package sk.jacob.engine.types;

import sk.jacob.engine.handler.Required;

/**
 * Fakt neviem naco je v kazdom nazve triedy ***Type
 *
 * @author mdzurik
 */
public class RequestHeader {
    private String messageId;

    @Required
    private String type;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
