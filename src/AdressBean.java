import com.sun.jndi.cosnaming.IiopUrl;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 * Created by stas on 4/7/15.
 */

import java.util.ArrayList;
import java.util.List;

@ManagedBean
@ViewScoped
public class AdressBean {
    private List<Adress> addressList = new ArrayList<Adress>(); // getter+setter

    public void addAddress() {
        addressList.add(new Adress());
    }

    public void persistAddresses() {
        // store the addressList filled with addresses
    }
}
