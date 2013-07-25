package sk.jacob.mpu.context.tenant;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import sk.jacob.annotation.Required;
import sk.jacob.common.CONTEXT;
import sk.jacob.engine.handler.Message;
import sk.jacob.mpu.context.model.ContextModel;
import sk.jacob.mpu.context.model.Tenants;
import sk.jacob.mpu.context.model.UsersTenants;
import sk.jacob.sql.dml.SqlClause;
import sk.jacob.sql.engine.Connection;
import sk.jacob.sql.engine.JacobResultSet;
import sk.jacob.types.DataPacket;
import sk.jacob.types.RequestDataType;
import sk.jacob.types.ResponseDataType;
import sk.jacob.types.Return;
import static sk.jacob.sql.dml.Op.eq;
import static sk.jacob.sql.dml.DML.select;

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

            public TenantResponse(String id, String name) {
                this.tenantId = id;
                this.tenantName = name;
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

        Tenants tenants = ContextModel.INSTANCE.table(Tenants.class);
        UsersTenants usersTenants = ContextModel.INSTANCE.table(UsersTenants.class);

        SqlClause s = select(tenants.id, tenants.name)
                .from(tenants)
                .join(usersTenants, eq(tenants.id, usersTenants.tenantFk))
                .where(eq(usersTenants.login, requestData.login));
        Connection conn = (Connection) CONTEXT.CONNECTION.get(dataPacket);
        JacobResultSet rs = (JacobResultSet) conn.execute(s);

        List<FindByLoginResd.TenantResponse> responseTenants = new LinkedList<>();
        while (rs.next()) {
            responseTenants.add(
                    new FindByLoginResd.TenantResponse(
                            rs.getString(tenants.id),
                            rs.getString(tenants.name)));
        }

        return Return.RESPONSE(new FindByLoginResd(requestData.login, responseTenants), dataPacket);
    }
}
