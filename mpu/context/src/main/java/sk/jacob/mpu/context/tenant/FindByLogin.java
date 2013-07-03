package sk.jacob.mpu.context.tenant;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import sk.jacob.engine.handler.Message;
import sk.jacob.engine.types.*;
import sk.jacob.engine.types.RequestDataType;
import sk.jacob.mpu.context.Context;
import static sk.jacob.sql.dml.DML.select;
import sk.jacob.sql.dml.DMLStatement;
import static sk.jacob.sql.dml.Op.eq;
import sk.jacob.sql.engine.ExecutionContext;

public class FindByLogin {
    private static class FindByLoginReqd extends RequestDataType {
        public String login;
    }

    private static class FindByLoginResd extends ResponseDataType {
        public static class TenantResponse {
            public String tenantId;
            public String tenantName;

            public TenantResponse(String id) {
                this.tenantId = id;
                this.tenantName = id;
            }
        }

        public String login;
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

        //kym nie je implementovany JOIN tak aspon takto...
        DMLStatement s = select("login", "tenant_fk")
                .from("users_tenants")
                .where(eq("login", requestData.login));

        ExecutionContext ectx = (ExecutionContext) Context.EXECUTION_CTX.get(dataPacket);
        ResultSet rs = (ResultSet) ectx.execute(s);

        List<FindByLoginResd.TenantResponse> responseTenants = new LinkedList<>();
        while (rs.next()) {
            responseTenants.add(new FindByLoginResd.TenantResponse(rs.getString("tenant_fk")));
        }

        return Return.OK(new FindByLoginResd(requestData.login, responseTenants), dataPacket);
    }
}
