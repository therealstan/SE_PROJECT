import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name="order")
@SessionScoped
public class OrderBean implements Serializable{

    private static final long serialVersionUID = 1L;




    String impactName;
    String impactValue;


    //getter and setter methods
    public String getImpactName() {
        return impactName;
    }

    public String getImpactValue() {
        return impactValue;
    }



    public void setImpactName(String impactName) {
        this.impactName = impactName;
    }

    public void setImpactValue(String impactValue) {
        this.impactValue = impactValue;
    }


    private static final ArrayList<Order> orderList =
            new ArrayList<Order>(Arrays.asList(

                    new Order("A0001", "Intel CPU"
                    ),
                    new Order("A0002", "Harddisk 10TB"
                    ),
                    new Order("A0003", "Dell Laptop"
                    ),
                    new Order("A0004", "Samsung LCD"
                    ),
                    new Order("A0005", "A4Tech Mouse"
                    )
            ));

    public ArrayList<Order> getOrderList() {

        return orderList;

    }

    public String addAction() {

        Order order = new Order(this.impactName, this.impactValue);

        orderList.add(order);
        return null;
    }

    public String deleteAction(Order order) {

        orderList.remove(order);
        return null;
    }

    public static class Order{


        private String impactValue;
        private String impactName;


        public Order(String impactName, String impactValue) {
            this.impactName = impactName;
            this.impactValue = impactValue;

        }

        //getter and setter methods


        public String getImpactValue() {
            return impactValue;
        }



        public void setImpactName(String impactName) {
            this.impactName = impactName;
        }

        public void setImpactValue(String impactValue) {
            this.impactValue = impactValue;
        }


        public String getImpactName() {
            return impactName;
        }
    }
}