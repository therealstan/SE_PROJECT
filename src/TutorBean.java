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

    String impact1Name;
    String impact2Name;
    String impact3Name;
    double impact1;
    double impact2;
    double impact3;
    private DatabaseCon dbCon;
    private List<S2G> s2gList;
    private String selectedS2G;
    private boolean editmode = false;


/// NEW VERSION
    String impactName;
    double impact;

    private static final ArrayList<Impact> impactList =
            new ArrayList<>(Arrays.asList(
                    new Impact("Item1", 0.54),
                    new Impact("Item2", 0.57)
            ));

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

    public static class Impact{

        public String getImpactName() {
            return impactName;
        }

        public void setImpactName(String impactName) {
            this.impactName = impactName;
        }

        public double getImpact() {
            return impact;
        }

        public void setImpact(double impact) {
            this.impact = impact;
        }

        String impactName;
        double impact;


        public Impact(String impactName, double impact) {
            this.impactName = impactName;
            this.impact = impact;
        }





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


    /*
    ToDo:
    extrem unschön
     */

    public String getImpact1Name() {
        return impact1Name;
    }

    public void setImpact1Name(String impact1Name) {
        this.impact1Name = impact1Name;
    }

    public String getImpact2Name() {
        return impact2Name;
    }

    public void setImpact2Name(String impact2Name) {
        this.impact2Name = impact2Name;
    }

    public String getImpact3Name() {
        return impact3Name;
    }

    public void setImpact3Name(String impact3Name) {
        this.impact3Name = impact3Name;
    }

    public double getImpact1() {
        return impact1;
    }

    public void setImpact1(double impact1) {
        this.impact1 = impact1;
    }

    public double getImpact2() {
        return impact2;
    }

    public void setImpact2(double impact2) {
        this.impact2 = impact2;
    }

    public double getImpact3() {
        return impact3;
    }

    public void setImpact3(double impact3) {
        this.impact3 = impact3;
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

//        S2G tt = new S2G(1.0,0,1.0,7.0);
//        tt.setDescription("DHBW");
//        try {
//            tt.serialize(dbCon);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

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
        r2s.addImpact(impact1Name, impact1);
        r2s.addImpact(impact2Name, impact2);
        r2s.addImpact(impact3Name, impact3);

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

