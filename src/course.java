/**
 * Created by stas on 4/27/15.
 */

import java.io.Serializable;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "course")
@RequestScoped
public class course implements Serializable{
    @ManagedProperty("#{param.courseID}")



    private String courseID;
    private String courseName;

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getCourseID() {
        return courseID;
    }
    public String getCourseName() {
        return courseName;
    }
}
