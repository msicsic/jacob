package sk.jacob.mpu.context.tenant;

import sk.jacob.appcommon.annotation.Resource;
import sk.jacob.appcommon.types.Message;
import sk.jacob.engine.handler.DataTypes;
import sk.jacob.mpu.context.model.*;
import sk.jacob.sql.dml.SqlClause;
import sk.jacob.sql.engine.Connection;
import sk.jacob.sql.engine.JacobResultSet;
import sk.jacob.appcommon.types.Principal;
import sk.jacob.appcommon.types.RequestData;
import sk.jacob.appcommon.types.ResponseData;

import java.util.Map;

import static sk.jacob.sql.dml.DML.*;
import static sk.jacob.sql.dml.Op.eq;

public class Create {
    private static class CreateTenantReqd extends RequestData {
        public String tenantName;
        public Map<String, String> params;
    }

    private static class CreateTenantResd extends ResponseData {
        public String tenantId;
        public String tenantName;
    }

    @DataTypes(type = "context.tenant.create",
               version = "1.0")
    public static Message<CreateTenantReqd, CreateTenantResd> handle (
            Message<CreateTenantReqd, CreateTenantResd> message,
            @Resource(location = "/abc") Connection conn,
            @Resource(location = "/Principal") Principal principal
    ) throws Exception {
        String tenantName = message.request.reqd.tenantName;
        String tenantId = tenantName.toUpperCase().replace(" ", "");

        // 1. Create tenant entry
        Tenants tenants = ContextModel.INSTANCE.table(Tenants.class);
        conn.execute(insert(tenants).values(cv(tenants.id, tenantId),
                                            cv(tenants.name, tenantName)));

        // 2. Create tenant user association
        UsersTenants usersTenants = ContextModel.INSTANCE.table(UsersTenants.class);
        conn.execute(insert(usersTenants).values(cv(usersTenants.login, principal.login),
                                                 cv(usersTenants.tenantFk, tenantId)));

        // 3. Insert tenant creation parameters
        TenantsParams tenantsParams = ContextModel.INSTANCE.table(TenantsParams.class);
        for(Map.Entry<String, String> entry :  message.request.reqd.params.entrySet()) {
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

        // 5. Associate tenant with DS
        String paramValue = String.format("%s.%s", datasource_id, "context.lds_bds_schema");
        conn.execute(insert(tenantsParams).values(cv(tenantsParams.tenantFk, tenantId),
                                                  cv(tenantsParams.paramName, "lds/BDS"),
                                                  cv(tenantsParams.paramValue, paramValue),
                                                  cv(tenantsParams.scope, ParamScope.PRIVATE .name())));

        return message;
    }
}
