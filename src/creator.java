import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by stas on 4/7/15.
 */

@ManagedBean
@ViewScoped
public class creator {
    private List<String> values;

    @PostConstruct
    public void init() {
        values = new ArrayList();
        values.add("");
    }

    public void submit() {
        // save values in database
    }

    public void extend() {
        values.add("");
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public List<String> getValues() {
        return values;
    }
}
