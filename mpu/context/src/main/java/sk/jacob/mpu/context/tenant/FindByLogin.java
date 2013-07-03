package sk.jacob.mpu.context.tenant;

import java.util.LinkedList;
import java.util.List;
import sk.jacob.engine.handler.Message;
import sk.jacob.types.DataPacket;
import sk.jacob.types.RequestDataType;
import sk.jacob.types.ResponseDataType;
import sk.jacob.types.Return;

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
            version = "1.0",
            reqd = FindByLoginReqd.class,
            resd = FindByLoginResd.class)
    public static DataPacket handle(DataPacket dataPacket) {
        FindByLoginReqd requestData = (FindByLoginReqd) dataPacket.message.request.reqd;

        List<FindByLoginResd.TenantResponse> responseTenants = new LinkedList<FindByLoginResd.TenantResponse>();
        responseTenants.add(new FindByLoginResd.TenantResponse("1"));
        responseTenants.add(new FindByLoginResd.TenantResponse("2"));
        responseTenants.add(new FindByLoginResd.TenantResponse("3"));

        return Return.RESPONSE(new FindByLoginResd("Aaaa", responseTenants), dataPacket);
    }
}
