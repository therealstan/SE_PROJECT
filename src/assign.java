import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.*;
import java.util.List;

/**
 * Created by stas on 5/14/15.
 */
@ManagedBean(name="assign")
@ViewScoped
public class assign{


    public void setUser(User user) {
        this.user = user;
    }

    @ManagedProperty("#{user}")
    private User user;

    private long selectedUni;

    public long getSelectedFach() {
        return selectedFach;
    }

    public void setSelectedFach(long selectedFach) {
        this.selectedFach = selectedFach;
    }

    private long selectedFach;

    public List<DatabaseElement> getAvailableFach() {
        return availableFach;
    }

    public void setAvailableFach(List<DatabaseElement> availableFach) {
        this.availableFach = availableFach;
    }

    private List<DatabaseElement> availableFach;


    public User getUser() {
        return user;
    }



    public void changeUniversity(AjaxBehaviorEvent event) {

        availableFach = user.getDbCon().getFachrichtungList(user.getUserID(),selectedUni);
        selectedFach= 0;
 }

    public void setSelectedUni(long selectedUni) {
        this.selectedUni = selectedUni;
    }

    public long getSelectedUni() {
        return selectedUni;
    }
}
