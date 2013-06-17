package sk.jacob.engine.handler.devel;

import java.util.List;
import sk.jacob.engine.types.ResponseType;

public class MpuListResponseType extends ResponseType {
    private Object resh;

    private List resd;

    public Object getResh() {
        return resh;
    }

    public void setResh(Object resh) {
        this.resh = resh;
    }

    public List getResd() {
        return resd;
    }

    public void setResd(List resd) {
        this.resd = resd;
    }
}
