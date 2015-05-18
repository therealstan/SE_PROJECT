import java.io.Serializable;

/**
 * Created by Dom on 14.05.2015.
 */


/*
    class to hold parameters for Impact
    Name
    Impact
    Rate
 */
public class Impact implements Serializable{

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

    public double getRate() { return rate; }

    public void setRate(double rate) { this.rate = rate; }

    String impactName;
    double impact;
    double rate;


    public Impact(String impactName, double impact) {
        this.impactName = impactName;
        this.impact = impact;
    }
}