/**
 * Created by stas on 5/18/15.
 */
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
@ManagedBean(name = "studID")
@RequestScoped

public class studID {

    public long getStudID() {
        return studID;
    }

    public void setStudID(long studID) {
        this.studID = studID;
    }

    @ManagedProperty("#{param.studID}")
    public long studID;
}
