package sk.jacob.mpu.context.tenant;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import sk.jacob.annotation.Required;
import sk.jacob.engine.handler.Message;
import sk.jacob.common.CONTEXT;
import sk.jacob.sql.dml.DMLStatement;
import sk.jacob.sql.engine.Connection;
import sk.jacob.types.DataPacket;
import sk.jacob.types.RequestDataType;
import sk.jacob.types.ResponseDataType;
import sk.jacob.types.Return;
import static sk.jacob.sql.dml.Op.eq;
import static sk.jacob.sql.dml.DML.select;
import static sk.jacob.util.Log.logger;
import static sk.jacob.util.Log.sout;

public class FindByLogin {
    private static class FindByLoginReqd extends RequestDataType {
        @Required
        public String login;
    }

    private static class FindByLoginResd extends ResponseDataType {
        public static class TenantResponse {
            @Required
            public String tenantId;
            @Required
            public String tenantName;

            public TenantResponse(String id) {
                this.tenantId = id;
                this.tenantName = id;
            }
        }

        @Required
        public String login;
        @Required
        public List<TenantResponse> tenants;

        public FindByLoginResd(String login, List<TenantResponse> tenants) {
            this.login = login;
            this.tenants = tenants;
        }
    }

    @Message(type = "context.tenant.findByLogin",
             version = "1.0",
             reqd = FindByLoginReqd.class,
             resd = FindByLoginResd.class)
    public static DataPacket handle(DataPacket dataPacket) throws Exception {
        FindByLoginReqd requestData = (FindByLoginReqd) dataPacket.message.request.reqd;

        sout("requestData" + requestData.login);
        //kym nie je implementovany JOIN tak aspon takto...
        DMLStatement s = select("login", "tenant_fk")
                         .from("users_tenants")
                         .where(eq("login", requestData.login));

        Connection conn = (Connection) CONTEXT.CONNECTION.get(dataPacket);
        ResultSet rs = (ResultSet) conn.execute(s);

        List<FindByLoginResd.TenantResponse> responseTenants = new LinkedList<>();
        while (rs.next()) {
            responseTenants.add(new FindByLoginResd.TenantResponse(rs.getString("tenant_fk")));
        }

        return Return.RESPONSE(new FindByLoginResd(requestData.login, responseTenants), dataPacket);
    }
}
