import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dom on 17.11.2014.
 */

@ManagedBean(name = "student")
@SessionScoped
public class StudentBean {


    public List<Double> getGrades(User user) {

        DatabaseCon dbCon = user.getDbCon();

        List<Double> noteList = new ArrayList<Double>();

        if (user.getRole() == DatabaseCon.userRole.STUDENT) {
            noteList = dbCon.getGrade(user.getUserID());
        }
        return noteList;
    }
}
