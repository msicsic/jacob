package sk.jacob.appcommon.types;

import sk.jacob.appcommon.accessor.COMMON;
import sk.jacob.common.util.locale.Bundle;

import java.util.Locale;

import static sk.jacob.common.util.StackTrace.stackToString;

public class Return {
    public static ExecutionContext ERROR(String errorCode, ExecutionContext ec) {
        return ERROR(errorCode, Bundle.getMessage(errorCode, new Locale("SK")), ec);
    }

    public static ExecutionContext ERROR(String errorCode, String errorText, ExecutionContext ec) {
        return ERROR_AND_EXCEPTION("ERROR", errorCode, errorText, ec);
    }

    public static ExecutionContext EXCEPTION(String exceptionCode, ExecutionContext ec) {
        return EXCEPTION(exceptionCode, Bundle.getMessage(exceptionCode, new Locale("SK")), ec);
    }

    public static ExecutionContext EXCEPTION(String exceptionCode, Throwable throwable, ExecutionContext ec) {
        String stackTrace = null;

        try {
            stackTrace = stackToString(throwable);
        } catch (java.lang.Exception e) {
            stackTrace = exceptionCode;
        }
        return EXCEPTION(exceptionCode, stackTrace, ec);
    }

    public static ExecutionContext EXCEPTION(String exceptionCode, String exceptionText, ExecutionContext ec) {
        return ERROR_AND_EXCEPTION("EXCEPTION", exceptionCode, exceptionText, ec);
    }

    private static ExecutionContext ERROR_AND_EXCEPTION(String reason, String code, String text, ExecutionContext ec) {
        ec = FIN(ec);
        Message message = initResponse(ec);
        message.response.resh.status = MESSAGE_STATUS.INT.name();

        Exception exception = new Exception();
        message.response.resd = exception;
        exception.reason = reason;
        exception.code = code;
        exception.text = text;
        return ec;
    }

    public static ExecutionContext RESPONSE(ResponseData responseData, ExecutionContext ec) {
        ec = FIN(ec);
        Message message = initResponse(ec);
        message.response.resh.status = MESSAGE_STATUS.OK.name();
        message.response.resh.messageId = message.request.reqh.messageId;
        message.response.resd = responseData;
        return ec;
    }

    public static ExecutionContext EMPTY_RESPONSE(ExecutionContext ec) {
        return RESPONSE(new ResponseData(){}, ec);
    }

    private static Message initResponse(ExecutionContext ec) {
        Message message = COMMON.getMessage(ec);
        message.response = new Response();
        message.response.resh = new ResponseHeader();
        return message;
    }

    private static ExecutionContext FIN(ExecutionContext ec) {
        ec.status = EXECUTION_CONTEXT.FIN;
        return ec;
    }

    private static ExecutionContext AFP(ExecutionContext ec) {
        ec.status = EXECUTION_CONTEXT.AFP;
        return ec;
    }
}
