/**
 * Created by stas on 5/7/15.
 */
import java.io.Serializable;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
@ManagedBean(name = "uniID")
@RequestScoped

public class uniID {
    public String getUniID() {
        return uniID;
    }

    public void setUniID(String uniID) {
        this.uniID = uniID;
    }
    @ManagedProperty("#{param.uniID}")
    public String uniID;
}
