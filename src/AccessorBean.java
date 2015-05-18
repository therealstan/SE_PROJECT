import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIOutput;
import javax.faces.event.AjaxBehaviorEvent;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dom on 18.11.2014.
 */

/*
    Instructions:

    Use onPageLoaded(DatabaseCon dbCon, long courseID) to initialize required parameters
    Use getImpactList() to get a list with the saved Impacts from DB (Impact contains name + impact)
    Produce some kind of UIElement List with these impacts
    Iterate over impacts, add to each impact a field for the rate
    Use setRate(Impact impact, double rate) to add a rate to the given impact
    Use calcGrade() to calculate the current grade
    Use submitGrade(DatabaseCon dbCon, long studentID) to submit the grade to the DB
 */

@ManagedBean(name = "accessor")
@SessionScoped
public class AccessorBean {

    long courseID;
    long templateID;

    public float getNewRate() {
        return newRate;
    }

    public void setNewRate(float newRate) {
        this.newRate = newRate;
    }

    private float newRate;

    H2 h2;
    S2G s2g;
    R2S r2s;

    UIOutput gradeOutputText;
    private boolean h2Boolean;
    private String grade;

    public AccessorBean() {
        gradeOutputText = new UIOutput();
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public boolean getH2Boolean() {
        return h2Boolean;
    }

    public void setH2Boolean(boolean h2Boolean) {

        this.h2Boolean = h2Boolean;
    }

    private long getTemplateID(DatabaseCon dbCon, long courseID) {
        PreparedStatement ps;
        Connection con;
        DataSource ds = dbCon.getDs();

        if (ds != null) {
            try {
                con = ds.getConnection();
                if (con != null) {
                    String sql = "SELECT templateID FROM course WHERE courseID = (?)";
                    ps = con.prepareStatement(sql);
                    ps.setLong(1, courseID);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        templateID = rs.getInt(1);
                    }
                }
            } catch (SQLException sqle) {
                System.out.println("Kann mich nicht verbinden");
                sqle.printStackTrace();
            }
        }
        return templateID;
    }

    private void getObjects(DatabaseCon dbCon, long templateID) {
        PreparedStatement ps;
        Connection con;
        DataSource ds = dbCon.getDs();

        if (ds != null) {
            try {
                con = ds.getConnection();
                if (con != null) {
                    String sql = "SELECT h2_ID,r2s_ID,s2g_ID FROM template WHERE id = (?)";
                    ps = con.prepareStatement(sql);
                    ps.setLong(1, templateID);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        h2 = (H2) dbCon.deSerializeObject(rs.getInt(1));
                        r2s = (R2S) dbCon.deSerializeObject(rs.getInt(2));
                        s2g = (S2G) dbCon.deSerializeObject(rs.getInt(3));
                    }
                }
            } catch (SQLException sqle) {
                System.out.println("Kann mich nicht verbinden");
                sqle.printStackTrace();
            }
        }
    }

    /*
      return list of saved impacts
     */
    public List<Impact> getImpactList()
    {
        return r2s.impactList;
    }

    /*
        initialize class
        get template and saved objects (R2S, S2G, H2)
     */
    boolean loaded = false;
    public void onPageLoaded(DatabaseCon dbCon, long courseID) {
        if(loaded == false){
            templateID =  getTemplateID(dbCon, courseID);
            this.courseID = courseID;
            getObjects(dbCon, templateID);
            loaded = true;
        }
    }

    public UIOutput getGradeOutputText() {
        return gradeOutputText;
    }

    public void setGradeOutputText(UIOutput gradeOutputText) {
        this.gradeOutputText = gradeOutputText;
    }

    /*
        adds/ sets the rate to the given impact
     */
    public void setRate(Impact impact, double rate)
    {
        r2s.addRate(impact, rate);
    }

    /*
        calculates the grade from the given parameters (parameters are intern in class)
     */
    public void calcGrade() {
        double score = 0;
        try {
            score = r2s.getScore(h2Boolean, h2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        grade = String.valueOf(s2g.getGrade(score));
        gradeOutputText.setValue(grade);
    }

    /*
        submit and write grade to DB
     */
    public String submitGrade(DatabaseCon dbCon, long studentID) {
        if (grade != null) {
            dbCon.setGrade(courseID, studentID, Double.parseDouble(grade));
            dbCon.finishTemplate(templateID);
            return "success";
        } else
            return "error";
    }

    /*test*/
}
