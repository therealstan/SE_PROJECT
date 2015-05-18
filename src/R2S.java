import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dom on 11.11.2014.
 */
public class R2S implements Serializable {

    //list of impacts
    List<Impact> impactList;

    public List<Impact> getImpactList() { return impactList; }

    public R2S() {
        this.impactList = new ArrayList<Impact>();
    }

    public static long getID(DatabaseCon dbCon, long templateID) {
        long id = -1;

        DataSource ds = dbCon.getDs();

        PreparedStatement ps;
        Connection con;
        ResultSet rs;
        try {
            con = ds.getConnection();
            if (con != null) {
                String sql = "SELECT r2s_ID FROM template WHERE id = (?)";
                ps = con.prepareStatement(sql);
                ps.setLong(1, templateID);
                rs = ps.executeQuery();
                rs.next();

                id = rs.getInt("r2s_ID");
            }
        } catch (SQLException sqle) {
            System.out.println("Kann mich nicht verbinden");
            sqle.printStackTrace();
        }
        return id;
    }

    /*
        add new Impact
        For new impact only name and impact is required
     */
    public void addImpact(String name, double impact) {
        if (impact >= 0 && impact <= 1) {
            this.impactList.add(new Impact(name,impact));
        }
    }

    /*
        add rate to impact
     */
    public void addRate(Impact impact, double rate){

        for(int i = 0; i < impactList.size(); i++)
        {
            if(impactList.get(i) == impact)
            {
                impactList.get(i).setRate(rate);
                return;
            }
        }
        throw new IllegalArgumentException("Impact not found");
    }

    /*
      calculate the score
     */
    public double getScore(boolean b, H2 h2) throws Exception {
        double score = 0;
        if(!impactList.isEmpty()){
            score = h2.boolToScore(b);

            for(Impact element : impactList)
            {
                if(element.impact != Double.NaN)
                {
                    score = (1-Math.pow((element.impact*(1-score)),element.rate))/(1-element.impact*(1-score))*score;
                }
            }
        }
        return score;
    }


}
