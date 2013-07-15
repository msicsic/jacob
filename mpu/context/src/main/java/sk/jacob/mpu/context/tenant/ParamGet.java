package sk.jacob.mpu.context.tenant;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import sk.jacob.annotation.Required;
import sk.jacob.common.CONTEXT;
import sk.jacob.engine.handler.Message;
import sk.jacob.mpu.context.model.ContextModel;
import sk.jacob.mpu.context.model.TenantsParams;
import static sk.jacob.sql.dml.DML.select;
import static sk.jacob.sql.dml.Op.eq;
import static sk.jacob.sql.dml.Op.and;
import static sk.jacob.sql.dml.Op.in;
import sk.jacob.sql.dml.SqlClause;
import sk.jacob.sql.engine.Connection;
import sk.jacob.types.DataPacket;
import sk.jacob.types.RequestDataType;
import sk.jacob.types.ResponseDataType;
import sk.jacob.types.Return;

public class ParamGet {
    private static class ParamGetReqd extends RequestDataType {
        @Required
        public String tenantId;
        public Set<String> paramNames;
    }

    private static class ParamGetResd extends ResponseDataType {
        @Required
        public Map<String, String> paramValues;

        public ParamGetResd(Map<String, String> paramValues) {
            this.paramValues = paramValues;
        }
    }

    @Message(type = "context.tenant.paramGet",
            version = "1.0",
            reqd = ParamGet.ParamGetReqd.class,
            resd = ParamGet.ParamGetResd.class)
    public static DataPacket handle(DataPacket dataPacket) throws Exception {
        ParamGetReqd requestData = (ParamGetReqd) dataPacket.message.request.reqd;

        TenantsParams tenantsParams = ContextModel.table(TenantsParams.class);

        SqlClause s = select(tenantsParams.paramName, tenantsParams.paramValue)
                .from(tenantsParams)
                .where(and(eq(tenantsParams.tenantFk, requestData.tenantId),
                eq(tenantsParams.scope, "public"),
                in(tenantsParams.paramName, requestData.paramNames)));
        Connection conn = (Connection) CONTEXT.CONNECTION.get(dataPacket);
        ResultSet rs = (ResultSet) conn.execute(s);

        Map<String, String> paramValues = new HashMap<>();
        while (rs.next()) {
            paramValues.put(rs.getString(tenantsParams.paramName.name), rs.getString(tenantsParams.paramValue.name));
        }

        return Return.RESPONSE(new ParamGetResd(paramValues), dataPacket);
    }
}
