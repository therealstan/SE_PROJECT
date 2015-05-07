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
@ManagedBean(name = "fachID")
@RequestScoped

public class testID implements Serializable {


    public String getFachID() {
        return fachID;
    }

    public void setFachID(String fachID) {
        this.fachID = fachID;
    }

    @ManagedProperty("#{param.fachID}")



        private String fachID;


    }


