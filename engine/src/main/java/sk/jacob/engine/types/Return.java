package sk.jacob.engine.types;

public class Return {
    public static DataPacket EXCEPTION(String exceptionCode, DataPacket dataPacket) {
        dataPacket.status = STATUS.FIN;
        MessageType message = initResponse(dataPacket);
        message.response.resh.status = "INT";

        ExceptionType exception = new ExceptionType();
        message.response.resd = exception;
        exception.reason = "EXCEPTION";
        exception.code = exceptionCode;
        return dataPacket;
    }

    public static DataPacket OK(ResponseDataType responseData, DataPacket dataPacket) {
        dataPacket.status = STATUS.FIN;
        MessageType message = initResponse(dataPacket);
        message.response.resh.status = "OK";
        message.response.resh.messageId = "12345678";

        message.response.resd = responseData;
        return dataPacket;
    }

    private static MessageType initResponse(DataPacket dataPacket) {
        MessageType message = dataPacket.message;
        message.response = new ResponseType();
        message.response.resh = new ResponseHeaderType();
        return message;
    }
}
