package sk.jacob.mpu.context.tenant;

import java.util.Map;
import sk.jacob.annotation.Required;
import sk.jacob.common.CONTEXT;
import sk.jacob.common.SECURITY;
import sk.jacob.engine.handler.Message;
import sk.jacob.mpu.context.model.ContextModel;
import sk.jacob.mpu.context.model.Tenants;
import sk.jacob.mpu.context.model.TenantsParams;
import sk.jacob.mpu.context.model.UsersTenants;
import sk.jacob.sql.dml.SqlClause;
import sk.jacob.sql.engine.Connection;
import sk.jacob.types.DataPacket;
import sk.jacob.types.Principal;
import sk.jacob.types.RequestDataType;
import sk.jacob.types.ResponseDataType;
import sk.jacob.util.Security;

import static sk.jacob.sql.dml.DML.*;
import static sk.jacob.sql.dml.DML.cv;

public class Create {
    private static class CreateTenantReqd extends RequestDataType {
        @Required
        public String name;
        public Map<String, String> params;
    }

    private static class CreateTenantResd extends ResponseDataType {
        @Required
        public String tenantId;
        @Required
        public String tenantName;
    }

    @Message(type = "context.tenant.create",
            version = "1.0",
            reqd = Create.CreateTenantReqd.class,
            resd = Create.CreateTenantResd.class)
    public static DataPacket handle(DataPacket dataPacket) {
        CreateTenantReqd requestData = (CreateTenantReqd) dataPacket.message.request.reqd;
        String tenantName = requestData.name;
        String tenantId = tenantName.toUpperCase().replace(" ", "");

        Tenants tenants = ContextModel.INSTANCE.table(Tenants.class);
        Connection conn = (Connection) CONTEXT.CONNECTION.get(dataPacket);

        // 1. Create tenant enttry
        SqlClause sql = insert(tenants).values(cv(tenants.id, tenantId),
                                               cv(tenants.name, tenantName));
        conn.execute(sql);

        UsersTenants usersTenants = ContextModel.INSTANCE.table(UsersTenants.class);
        // 2. Create tenant user association
        Principal principal = SECURITY.getPrincipal(dataPacket);
        sql = insert(usersTenants).values(cv(usersTenants.login, principal.login),
                                          cv(usersTenants.tenantFk, tenantId));
        conn.execute(sql);

        // 3. Insert tenant creation parameters
        TenantsParams tenantsParams = ContextModel.INSTANCE.table(TenantsParams.class);
        for(Map.Entry<String, String> entry :  requestData.params.entrySet()) {
            sql = insert(tenantsParams).values(cv(tenantsParams.tenantFk, tenantId),
                                               cv(tenantsParams.paramName, entry.getKey()),
                                               cv(tenantsParams.paramValue, entry.getValue()),
                                               cv(tenantsParams.scope, ParamScope.PUBLIC.name()));
            conn.execute(sql);
        }

        // 4. Create ds entry
        // 5. Create lds/BDS entry parameter

        return dataPacket;
    }
}
