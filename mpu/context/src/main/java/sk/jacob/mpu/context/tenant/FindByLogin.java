package sk.jacob.mpu.context.tenant;

import sk.jacob.engine.handler.annotation.Resource;
import sk.jacob.appcommon.types.*;
import sk.jacob.engine.handler.annotation.Handler;
import sk.jacob.mpu.context.model.ContextModel;
import sk.jacob.mpu.context.model.Tenants;
import sk.jacob.mpu.context.model.UsersTenants;
import sk.jacob.sql.dml.SqlClause;
import sk.jacob.sql.engine.Connection;
import sk.jacob.sql.engine.JacobResultSet;

import java.util.LinkedList;
import java.util.List;

import static sk.jacob.sql.dml.DML.select;
import static sk.jacob.sql.dml.Op.eq;

public class FindByLogin {
    private static class FindByLoginReqd extends RequestData {
        public String login;
    }

    private static class FindByLoginResd extends ResponseData {
        public static class TenantResponse {
            public String tenantId;
            public String tenantName;

            public TenantResponse(String id, String name) {
                this.tenantId = id;
                this.tenantName = name;
            }
        }
        public String login;
        public List<TenantResponse> tenants;

        public FindByLoginResd(String login, List<TenantResponse> tenants) {
            this.login = login;
            this.tenants = tenants;
        }
    }

    @Handler(type = "context.tenant.findByLogin", version = "1.0")
    public static FindByLoginResd tenantFindByLogin(
            FindByLoginReqd requestData,
            @Resource(location = "/Resources/context/connection") Connection conn
    ) throws Exception {
        Tenants tenants = ContextModel.INSTANCE.table(Tenants.class);
        UsersTenants usersTenants = ContextModel.INSTANCE.table(UsersTenants.class);

        SqlClause s = select(tenants.id, tenants.name)
                .from(tenants)
                .join(usersTenants, eq(tenants.id, usersTenants.tenantFk))
                .where(eq(usersTenants.login, requestData.login));
        JacobResultSet rs = (JacobResultSet) conn.execute(s);

        List<FindByLoginResd.TenantResponse> responseTenants = new LinkedList<>();
        while (rs.next()) {
            responseTenants.add(
                    new FindByLoginResd.TenantResponse(
                            rs.getString(tenants.id),
                            rs.getString(tenants.name)));
        }
        return new FindByLoginResd(requestData.login, responseTenants);
    }
}
