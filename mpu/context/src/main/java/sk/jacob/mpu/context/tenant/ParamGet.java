package sk.jacob.mpu.context.tenant;

import sk.jacob.appcommon.annotation.Required;
import sk.jacob.appcommon.annotation.Resource;
import sk.jacob.appcommon.types.*;
import sk.jacob.engine.handler.Handler;
import sk.jacob.mpu.context.model.ContextModel;
import sk.jacob.mpu.context.model.TenantsParams;
import sk.jacob.sql.dml.SqlClause;
import sk.jacob.sql.engine.Connection;

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

    @Handler(type = "context.tenant.paramGet", version = "1.0")
    public static ParamGetResd paramGet(
            ParamGetReqd requestData,
            @Resource(location = "/Resources/context/connection") Connection conn
    ) throws Exception {
        TenantsParams tenantsParams = ContextModel.INSTANCE.table(TenantsParams.class);

        SqlClause s = select(tenantsParams.paramName, tenantsParams.paramValue)
                .from(tenantsParams)
                .where(and(eq(tenantsParams.tenantFk, requestData.tenantId),
                           eq(tenantsParams.scope, ParamScope.PUBLIC),
                           in(tenantsParams.paramName, requestData.paramNames)));
        ResultSet rs = (ResultSet) conn.execute(s);

        Map<String, String> paramValues = new HashMap<>();
        while (rs.next()) {
            paramValues.put(rs.getString(tenantsParams.paramName.name), rs.getString(tenantsParams.paramValue.name));
        }
        return new ParamGetResd(paramValues);
    }
}
