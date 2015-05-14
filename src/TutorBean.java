/**
 * Created by Dom on 17.11.2014.
 */

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.Serializable;

@ManagedBean(name = "tutor")
@SessionScoped

public class TutorBean implements Serializable {

    String templateName;
    String h2Name;
    double h2Threshold;

    private DatabaseCon dbCon;
    private List<S2G> s2gList;
    private String selectedS2G;
    private boolean editmode = false;


    public void setImpactName(String impactName) {
        this.impactName = impactName;
    }

    public void setImpact(double impact) {
        this.impact = impact;
    }

    /// NEW VERSION
    String impactName;
    double impact;

    private static final ArrayList<Impact> impactList =
            new ArrayList<Impact>();

    public ArrayList<Impact> getImpactList() {

        return impactList;

    }

    public String addItem() {

        Impact impact = new Impact(this.impactName, this.impact);

       impactList.add(impact);
        return null;
    }

    public String deleteItem(Impact impact) {

        impactList.remove(impact);
        return null;
    }

      public String getImpactName() {
        return impactName;
    }

    public double getImpact() {
        return impact;
    }

/// END OF NEW VERSION




    public void edit() {

        this.editmode = true;
    }

    public void save() {
        this.editmode = false;
    }

    public TutorBean() {
        this.s2gList = new ArrayList<S2G>();
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public List<String> getS2GStringList(DatabaseCon dbCon) {
        List<String> list = new ArrayList<String>();

        PreparedStatement ps;
        Connection con;

        DataSource ds = dbCon.getDs();

        if (ds != null) {
            try {
                con = ds.getConnection();
                if (con != null) {
                    String sql = "SELECT id,name FROM data WHERE name = 'S2G'";
                    ps = con.prepareStatement(sql);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        String name = rs.getString("name");
                        S2G s2g = (S2G) dbCon.deSerializeObject(rs.getInt("id"));
                        s2gList.add(s2g);
                        list.add(s2g.getDescription());
                    }
                }
            } catch (SQLException sqle) {
                System.out.println("Kann mich nicht verbinden");
                sqle.printStackTrace();
            }
        }
        return list;
    }

    //region R2S

    public String getSelectedS2G() {
        return selectedS2G;
    }

    public void setSelectedS2G(String selectedS2G) {
        this.selectedS2G = selectedS2G;
    }

    public double getH2Threshold() {
        return h2Threshold;
    }

    public void setH2Threshold(double h2Threshold) {
        this.h2Threshold = h2Threshold;
    }

    public String getH2Name() {
        return h2Name;
    }

    public void setH2Name(String h2Name) {
        this.h2Name = h2Name;
    }
     //endregion

    private void updateTemplateID(DatabaseCon dbCon, long templateID, long accessorID) {
        PreparedStatement ps;
        Connection con;

        DataSource ds = dbCon.getDs();

        if (ds != null) {
            try {
                con = ds.getConnection();
                if (con != null) {
                    String sql = "UPDATE course SET templateID = (?) WHERE accessorID = (?)";
                    ps = con.prepareStatement(sql);
                    ps.setLong(1, templateID);
                    ps.setLong(2, accessorID);
                    ps.executeUpdate();
                }
            } catch (SQLException sqle) {
                System.out.println("Kann mich nicht verbinden");
                sqle.printStackTrace();
            }
        }
    }

    public String setValues(User user) {
        this.dbCon = user.getDbCon();

        H2 h2 = new H2();
        h2.setName(h2Name);
        h2.setP(h2Threshold);

        S2G s2g = null;

        for (S2G s2G : s2gList) {
            if (s2G.getDescription().equals(selectedS2G))
                s2g = s2G;
        }
        if (s2g == null)
            throw new IllegalArgumentException("no S2G selected");

        R2S r2s = new R2S();
        for(Impact element : impactList)
        {
            r2s.addImpact(element.impactName, element.impact);
        }
        long templateID = dbCon.addTemplate(templateName, h2, r2s, s2g);

        updateTemplateID(dbCon, templateID, 29);

        if (templateID != -1)
            return "success";
        else
            return null;
            /*
            Todo
            Error Page
             */
    }



}

