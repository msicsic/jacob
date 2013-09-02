package sk.jacob.types;

import sk.jacob.common.MESSAGE;
import sk.jacob.util.locale.MessageResolver;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;

public class Return {
    public static ExecutionContext ERROR(String errorCode, ExecutionContext executionContext) {
        return ERROR(errorCode, MessageResolver.getMessage(errorCode, new Locale("SK")), executionContext);
    }

    public static ExecutionContext ERROR(String errorCode, String errorText, ExecutionContext executionContext) {
        return ERROR_AND_EXCEPTION("ERROR", errorCode, errorText, executionContext);
    }

    public static ExecutionContext EXCEPTION(String exceptionCode, ExecutionContext executionContext) {
        return EXCEPTION(exceptionCode, MessageResolver.getMessage(exceptionCode, new Locale("SK")), executionContext);
    }

    public static ExecutionContext EXCEPTION(String exceptionCode, Throwable throwable, ExecutionContext executionContext) {
        String stackTrace = null;

        try {
            stackTrace = stackToString(throwable);
        } catch (java.lang.Exception e) {
            stackTrace = exceptionCode;
        }
        return EXCEPTION(exceptionCode, stackTrace, executionContext);
    }

    public static ExecutionContext EXCEPTION(String exceptionCode, String exceptionText, ExecutionContext executionContext) {
        return ERROR_AND_EXCEPTION("EXCEPTION", exceptionCode, exceptionText, executionContext);
    }

    private static ExecutionContext ERROR_AND_EXCEPTION(String reason, String code, String text, ExecutionContext executionContext) {
        executionContext = FIN(executionContext);
        MessageType message = initResponse(executionContext);
        message.response.resh.status = MESSAGE_STATUS.INT.name();

        Exception exception = new Exception();
        message.response.resd = exception;
        exception.reason = reason;
        exception.code = code;
        exception.text = text;
        return executionContext;
    }

    public static String stackToString(Throwable t) {
        StringWriter stringWriter = new StringWriter();
        t.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    public static ExecutionContext RESPONSE(ResponseData responseData, ExecutionContext executionContext) {
        executionContext = FIN(executionContext);
        MessageType message = initResponse(executionContext);
        message.response.resh.status = MESSAGE_STATUS.OK.name();
        message.response.resh.messageId = message.request.reqh.messageId;
        message.response.resd = responseData;
        return executionContext;
    }

    public static ExecutionContext EMPTY_RESPONSE(ExecutionContext executionContext) {
        return RESPONSE(new ResponseData(){}, executionContext);
    }

    private static MessageType initResponse(ExecutionContext executionContext) {
        MessageType message = MESSAGE.current(executionContext);
        message.response = new Response();
        message.response.resh = new ResponseHeader();
        return message;
    }

    private static ExecutionContext FIN(ExecutionContext executionContext) {
        executionContext.status = DATAPACKET_STATUS.FIN;
        return executionContext;
    }

    private static ExecutionContext AFP(ExecutionContext executionContext) {
        executionContext.status = DATAPACKET_STATUS.AFP;
        return executionContext;
    }
}
