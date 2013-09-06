package sk.jacob.mpu.context.tenant;

import sk.jacob.appcommon.accessor.COMMON;
import sk.jacob.appcommon.accessor.CONTEXT;
import sk.jacob.appcommon.annotation.Required;
import sk.jacob.engine.handler.DataTypes;
import sk.jacob.mpu.context.model.ContextModel;
import sk.jacob.mpu.context.model.TenantsParams;
import sk.jacob.sql.dml.SqlClause;
import sk.jacob.sql.engine.Connection;
import sk.jacob.appcommon.types.ExecutionContext;
import sk.jacob.appcommon.types.RequestData;
import sk.jacob.appcommon.types.ResponseData;
import sk.jacob.appcommon.types.Return;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static sk.jacob.sql.dml.DML.select;
import static sk.jacob.sql.dml.Op.*;

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

    @DataTypes(type = "context.tenant.paramGet",
               version = "1.0",
               reqd = ParamGetReqd.class,
               resd = ParamGetResd.class)
    public static ExecutionContext handle(ExecutionContext ec) throws Exception {
        ParamGetReqd requestData = (ParamGetReqd)COMMON.MESSAGE.getFrom(ec).request.reqd;

        TenantsParams tenantsParams = ContextModel.INSTANCE.table(TenantsParams.class);

        SqlClause s = select(tenantsParams.paramName, tenantsParams.paramValue)
                .from(tenantsParams)
                .where(and(eq(tenantsParams.tenantFk, requestData.tenantId),
                           eq(tenantsParams.scope, ParamScope.PUBLIC),
                           in(tenantsParams.paramName, requestData.paramNames)));
        Connection conn = CONTEXT.CONNECTION.getFrom(ec);
        ResultSet rs = (ResultSet) conn.execute(s);

        Map<String, String> paramValues = new HashMap<>();
        while (rs.next()) {
            paramValues.put(rs.getString(tenantsParams.paramName.name), rs.getString(tenantsParams.paramValue.name));
        }

        return Return.RESPONSE(new ParamGetResd(paramValues), ec);
    }
}
