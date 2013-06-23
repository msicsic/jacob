package sk.jacob.mpu.context.tenant;

import java.util.LinkedList;
import java.util.List;
import sk.jacob.engine.handler.Message;
import sk.jacob.engine.types.DataPacket;
import sk.jacob.engine.types.RequestData;
import sk.jacob.engine.types.RequestHeader;
import sk.jacob.engine.types.RequestType;
import sk.jacob.engine.types.ResponseDataType;
import sk.jacob.engine.types.ResponseHeaderType;
import sk.jacob.engine.types.ResponseType;

public class FindByLogin {
    public static class FindByLoginRequestData implements RequestData {
        String login;

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }
    }

    public static class ResponseTenant {
        private String tenantId;

        private String tenantName;

        public ResponseTenant(String id) {
            this.tenantId = id;
            this.tenantName = "Name of " + id;
        }

        public String getTenantId() {
            return tenantId;
        }

        public void setTenantId(String tenantId) {
            this.tenantId = tenantId;
        }

        public String getTenantName() {
            return tenantName;
        }

        public void setTenantName(String tenantName) {
            this.tenantName = tenantName;
        }
    }

    public static class FindByLoginResponseData implements ResponseDataType {
        private String login;

        private List<ResponseTenant> tenants;

        public FindByLoginResponseData(String login, List<ResponseTenant> tenants) {
            this.login = login;
            this.tenants = tenants;
        }

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public List<ResponseTenant> getTenants() {
            return tenants;
        }

        public void setTenants(List<ResponseTenant> tenants) {
            this.tenants = tenants;
        }
    }

    public static class FindByLoginRequest extends RequestType<RequestHeader, FindByLoginRequestData> {
    }

    public static class FindByLoginResponse extends ResponseType<ResponseHeaderType, FindByLoginResponseData> {
    }

    //TODO dost dlho mi trvalo kym som prisiel na to ze metoda musi byt staticka...
    @Message(type = "context.tenant.findByLogin",
            version = "0.1",
            reqd = FindByLoginRequest.class,
            resd = FindByLoginResponse.class)
    public static DataPacket handle(DataPacket dataPacket) {
        FindByLoginRequestData requestData = (FindByLoginRequestData) dataPacket.message.request.reqd;

        List<ResponseTenant> responseTenants = new LinkedList<>();
        responseTenants.add(new ResponseTenant("1"));
        responseTenants.add(new ResponseTenant("2"));
        responseTenants.add(new ResponseTenant("3"));

        dataPacket.message.createResponse(new FindByLoginResponseData(requestData.getLogin(), responseTenants));

        return dataPacket;
    }
}
