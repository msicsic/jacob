package sk.jacob.engine.types;

public class Return {
    public static DataPacket EXCEPTION(String exceptionCode, DataPacket dataPacket) {
        dataPacket = FINISH(dataPacket);
        MessageType message = initResponse(dataPacket);
        message.response.resh.status = MESSAGE_STATUS.INT.name();

        ExceptionType exception = new ExceptionType();
        message.response.resd = exception;
        exception.reason = "EXCEPTION";
        exception.code = exceptionCode;
        return dataPacket;
    }

    public static DataPacket OK(ResponseDataType responseData, DataPacket dataPacket) {
        dataPacket = FINISH(dataPacket);
        MessageType message = initResponse(dataPacket);
        message.response.resh.status = MESSAGE_STATUS.OK.name();
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

    public static DataPacket FINISH(DataPacket dataPacket) {
        dataPacket.dataPacketStatus = DATAPACKET_STATUS.FIN;
        return dataPacket;
    }
}
