package sk.jacob.mpu.context;

import com.google.gson.JsonObject;
import sk.jacob.appcommon.accessor.COMMON;
import sk.jacob.appcommon.accessor.CONFIG;
import sk.jacob.appcommon.accessor.CONTEXT;
import sk.jacob.appcommon.types.*;
import sk.jacob.engine.ApplicationModule;
import sk.jacob.mpu.context.model.ContextModel;
import sk.jacob.sql.Metadata;
import sk.jacob.sql.engine.Connection;
import sk.jacob.sql.engine.DbEngine;

import java.util.*;

import static sk.jacob.common.util.Log.logger;

public class ContextApplicationModule extends ApplicationModule {
    private static final Metadata MODEL = ContextModel.INSTANCE.METADATA;
    private final DbEngine dbEngine;
    private final Properties config;

    public ContextApplicationModule(Properties config) {
        super(handlerClasses());
        this.config = config;
        this.dbEngine = new DbEngine(CONFIG.CONTEXT_URL.get(config),
                                     CONFIG.CONTEXT_USERNAME.get(config),
                                     CONFIG.CONTEXT_PASSWORD.get(config));
        this.initDatabase();
    }

    private static List<Class> handlerClasses() {
        return Arrays.asList(sk.jacob.mpu.context.tenant.Init.HANDLERS);
    }

    @Override
    public ExecutionContext onRequest(ExecutionContext ec) {
        if (ec.status == EXECUTION_CONTEXT.FIN)
            return ec;

        Connection conn = this.dbEngine.getConnection();
        CONTEXT.CONNECTION.storeValue(conn, ec);
        CONTEXT.LDS_BDS.storeValue(new HashMap<CONFIG, String>() {{
            put(CONFIG.LDS_BDS_TEMPLATE_URL, CONFIG.LDS_BDS_TEMPLATE_URL.get(config));
            put(CONFIG.LDS_BDS_TEMPLATE_USERNAME, CONFIG.LDS_BDS_TEMPLATE_USERNAME.get(config));
            put(CONFIG.LDS_BDS_TEMPLATE_PASSWORD, CONFIG.LDS_BDS_TEMPLATE_PASSWORD.get(config));
        }}, ec);

        try {
            conn.txBegin();
            ec = super.process(ec);
            conn.txCommit();
        } catch (Exception e) {
            conn.txRollback();
            String errorCode = "context.general.context.exception";
            logger(this).error(errorCode, e);
            ec = Return.EXCEPTION(errorCode, e, ec);
        } finally {
            conn.close();
        }

        return ec;
    }

    private void initDatabase() {
        MODEL.createAll(this.dbEngine);
    }

    @Override
    protected String getHandlerKeyFromMessage(ExecutionContext ec) {
        RequestHeader rh = COMMON.MESSAGE.getFrom(ec).request.reqh;
        return rh.type + "." + rh.version;
    }

    @Override
    protected Object mapPayload(ExecutionContext ec, Class<?> payloadClass) {
        Message message = COMMON.MESSAGE.getFrom(ec);
        JsonObject jsonRequest = message.jsonRequest;
        RequestData payload = (RequestData)GSON.fromJson(jsonRequest.get("reqd"), payloadClass);
        message.request.reqd = payload;
        return payload;
    }
}