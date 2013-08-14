package sk.jacob.mpu.context.tenant;

import java.util.Map;
import sk.jacob.annotation.Required;
import sk.jacob.common.CONTEXT;
import sk.jacob.common.SECURITY;
import sk.jacob.engine.handler.Message;
import sk.jacob.mpu.context.model.*;
import sk.jacob.sql.dml.SqlClause;
import sk.jacob.sql.engine.Connection;
import sk.jacob.sql.engine.JacobResultSet;
import sk.jacob.types.DataPacket;
import sk.jacob.types.Principal;
import sk.jacob.types.RequestDataType;
import sk.jacob.types.ResponseDataType;

import static sk.jacob.sql.dml.DML.*;
import static sk.jacob.sql.dml.DML.cv;
import static sk.jacob.sql.dml.Op.eq;

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
    public static DataPacket handle(DataPacket dataPacket) throws Exception {
        CreateTenantReqd requestData = (CreateTenantReqd) dataPacket.message.request.reqd;
        String tenantName = requestData.name;
        String tenantId = tenantName.toUpperCase().replace(" ", "");
        Connection conn = (Connection) CONTEXT.CONNECTION.get(dataPacket);

        // 1. Create tenant entry
        Tenants tenants = ContextModel.INSTANCE.table(Tenants.class);
        conn.execute(insert(tenants).values(cv(tenants.id, tenantId),
                                            cv(tenants.name, tenantName)));

        // 2. Create tenant user association
        UsersTenants usersTenants = ContextModel.INSTANCE.table(UsersTenants.class);
        Principal principal = SECURITY.getPrincipal(dataPacket);
        conn.execute(insert(usersTenants).values(cv(usersTenants.login, principal.login),
                                                 cv(usersTenants.tenantFk, tenantId)));

        // 3. Insert tenant creation parameters
        TenantsParams tenantsParams = ContextModel.INSTANCE.table(TenantsParams.class);
        for(Map.Entry<String, String> entry :  requestData.params.entrySet()) {
            conn.execute(insert(tenantsParams).values(cv(tenantsParams.tenantFk, tenantId),
                                                      cv(tenantsParams.paramName, entry.getKey()),
                                                      cv(tenantsParams.paramValue, entry.getValue()),
                                                      cv(tenantsParams.scope, ParamScope.PUBLIC.name())));
        }

        // 4. Create ds entry
        Ds ds = ContextModel.INSTANCE.table(Ds.class);
        SqlClause sql = select(ds.id).from(ds).where(eq(ds.url, "context.lds_bds_url"));
        JacobResultSet rs =  (JacobResultSet)conn.execute(sql);
        Long datasource_id;
        if(rs.next() == false) {
            datasource_id = (Long)conn.execute(insert(ds).values(cv(ds.url, "context.lds_bds_url")));
        } else {
            datasource_id = rs.getLong(ds.id);
        }

        // 5. Create lds/BDS entry parameter
        String paramValue = String.format("%s.%s", datasource_id, "context.lds_bds_schema");
        conn.execute(insert(tenantsParams).values(cv(tenantsParams.tenantFk, tenantId),
                                          cv(tenantsParams.paramName, "lds/BDS"),
                                          cv(tenantsParams.paramValue, paramValue),
                                          cv(tenantsParams.scope, "private")));

        return dataPacket;
    }
}
