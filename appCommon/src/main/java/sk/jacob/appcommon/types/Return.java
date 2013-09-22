package sk.jacob.appcommon.types;

import sk.jacob.appcommon.accessor.COMMON;
import sk.jacob.common.util.locale.Bundle;

import java.util.Locale;

import static sk.jacob.common.util.StackTrace.stackToString;

public class Return {
    public static ExecutionContext FIN_RESPONSE(ResponseData responseData, ExecutionContext ec) {
        ec = FIN(ec);
        Message message = COMMON.MESSAGE.getFrom(ec);
        message.response.resh.status = MESSAGE_STATUS.OK.name();
        message.response.resh.messageId = message.request.reqh.messageId;
        message.response.resd = responseData;
        return ec;
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
