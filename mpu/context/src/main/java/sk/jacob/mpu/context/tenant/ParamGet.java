package sk.jacob.mpu.context.tenant;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import sk.jacob.annotation.Required;
import sk.jacob.common.CONTEXT;
import sk.jacob.common.MESSAGE;
import sk.jacob.engine.handler.Message;
import sk.jacob.mpu.context.model.ContextModel;
import sk.jacob.mpu.context.model.TenantsParams;
import static sk.jacob.sql.dml.DML.select;
import static sk.jacob.sql.dml.Op.eq;
import static sk.jacob.sql.dml.Op.and;
import static sk.jacob.sql.dml.Op.in;
import sk.jacob.sql.dml.SqlClause;
import sk.jacob.sql.engine.Connection;
import sk.jacob.types.ExecutionContext;
import sk.jacob.types.RequestData;
import sk.jacob.types.ResponseData;
import sk.jacob.types.Return;

public class ParamGet {
    private static class ParamGetReqd extends RequestData {
        @Required
        public String tenantId;
        public Set<String> paramNames;
    }

    private static class ParamGetResd extends ResponseData {
        @Required
        public Map<String, String> paramValues;

        public ParamGetResd(Map<String, String> paramValues) {
            this.paramValues = paramValues;
        }
    }

    @Message(type = "context.tenant.paramGet",
             version = "1.0",
             reqd = ParamGetReqd.class,
             resd = ParamGetResd.class)
    public static ExecutionContext handle(ExecutionContext executionContext) throws Exception {
        ParamGetReqd requestData = (ParamGetReqd) MESSAGE.current(executionContext).request.reqd;

        TenantsParams tenantsParams = ContextModel.INSTANCE.table(TenantsParams.class);

        SqlClause s = select(tenantsParams.paramName, tenantsParams.paramValue)
                .from(tenantsParams)
                .where(and(eq(tenantsParams.tenantFk, requestData.tenantId),
                           eq(tenantsParams.scope, "public"),
                           in(tenantsParams.paramName, requestData.paramNames)));
        Connection conn = (Connection) CONTEXT.CONNECTION.get(executionContext);
        ResultSet rs = (ResultSet) conn.execute(s);

        Map<String, String> paramValues = new HashMap<>();
        while (rs.next()) {
            paramValues.put(rs.getString(tenantsParams.paramName.name), rs.getString(tenantsParams.paramValue.name));
        }

        return Return.RESPONSE(new ParamGetResd(paramValues), executionContext);
    }
}
