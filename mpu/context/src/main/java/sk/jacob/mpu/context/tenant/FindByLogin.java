package sk.jacob.mpu.context.tenant;

import java.util.LinkedList;
import java.util.List;
import sk.jacob.engine.handler.Message;
import sk.jacob.engine.types.*;
import sk.jacob.engine.types.RequestDataType;

public class FindByLogin {
    public static class FindByLoginReqd extends RequestDataType {
        public String login;
    }

    public static class FindByLoginResd extends ResponseDataType {
        public static class TenantResponse {
            public String tenantId;
            public String tenantName;

            public TenantResponse(String id) {
                this.tenantId = id;
                this.tenantName = "Name of " + id;
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
             version = "0.1",
             reqd = FindByLoginReqd.class,
             resd = FindByLoginResd.class)
    public static DataPacket handle(DataPacket dataPacket) {
        FindByLoginReqd requestData = (FindByLoginReqd) dataPacket.message.request.reqd;

        List<FindByLoginResd.TenantResponse> responseTenants = new LinkedList<FindByLoginResd.TenantResponse>();
        responseTenants.add(new FindByLoginResd.TenantResponse("1"));
        responseTenants.add(new FindByLoginResd.TenantResponse("2"));
        responseTenants.add(new FindByLoginResd.TenantResponse("3"));

        dataPacket.message.createResponse(new FindByLoginResd(requestData.login, responseTenants));
        return dataPacket;
    }
}
