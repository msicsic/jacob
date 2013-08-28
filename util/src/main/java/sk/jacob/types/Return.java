package sk.jacob.types;

import sk.jacob.common.MESSAGE;
import sk.jacob.util.locale.MessageResolver;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;

public class Return {
    public static DataPacket ERROR(String errorCode, DataPacket dataPacket) {
        return ERROR(errorCode, MessageResolver.getMessage(errorCode, new Locale("SK")), dataPacket);
    }

    public static DataPacket ERROR(String errorCode, String errorText, DataPacket dataPacket) {
        return ERROR_AND_EXCEPTION("ERROR", errorCode, errorText, dataPacket);
    }

    public static DataPacket EXCEPTION(String exceptionCode, DataPacket dataPacket) {
        return EXCEPTION(exceptionCode, MessageResolver.getMessage(exceptionCode, new Locale("SK")), dataPacket);
    }

    public static DataPacket EXCEPTION(String exceptionCode, Throwable throwable, DataPacket dataPacket) {
        String stackTrace = null;

        try {
            stackTrace = stackToString(throwable);
        } catch (Exception e) {
            stackTrace = exceptionCode;
        }
        return EXCEPTION(exceptionCode, stackTrace, dataPacket);
    }

    public static DataPacket EXCEPTION(String exceptionCode, String exceptionText, DataPacket dataPacket) {
        return ERROR_AND_EXCEPTION("EXCEPTION", exceptionCode, exceptionText, dataPacket);
    }

    private static DataPacket ERROR_AND_EXCEPTION(String reason, String code, String text, DataPacket dataPacket) {
        dataPacket = FIN(dataPacket);
        MessageType message = initResponse(dataPacket);
        message.response.resh.status = MESSAGE_STATUS.INT.name();

        ExceptionType exception = new ExceptionType();
        message.response.resd = exception;
        exception.reason = reason;
        exception.code = code;
        exception.text = text;
        return dataPacket;
    }

    public static String stackToString(Throwable t) {
        StringWriter stringWriter = new StringWriter();
        t.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    public static DataPacket RESPONSE(ResponseDataType responseData, DataPacket dataPacket) {
        dataPacket = FIN(dataPacket);
        MessageType message = initResponse(dataPacket);
        message.response.resh.status = MESSAGE_STATUS.OK.name();
        message.response.resh.messageId = message.request.reqh.messageId;
        message.response.resd = responseData;
        return dataPacket;
    }

    public static DataPacket EMPTY_RESPONSE(DataPacket dataPacket) {
        return RESPONSE(new ResponseDataType(){}, dataPacket);
    }

    private static MessageType initResponse(DataPacket dataPacket) {
        MessageType message = MESSAGE.current(dataPacket);
        message.response = new ResponseType();
        message.response.resh = new ResponseHeaderType();
        return message;
    }

    private static DataPacket FIN(DataPacket dataPacket) {
        dataPacket.status = DATAPACKET_STATUS.FIN;
        return dataPacket;
    }

    private static DataPacket AFP(DataPacket dataPacket) {
        dataPacket.status = DATAPACKET_STATUS.AFP;
        return dataPacket;
    }
}
