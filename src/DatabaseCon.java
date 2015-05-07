import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dom on 31.10.2014.
 */
public class DatabaseCon {

    DataSource ds;

    private Connection con;

    public DatabaseCon() {
        try {
            Context ctx = new InitialContext();
            ds = (DataSource) ctx.lookup("java:comp/env/jdbc/database");
            con = ds.getConnection();
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public DataSource getDs() {
        return ds;
    }

    public void setGrade(long courseID, long studentID, double grade) {
        if (getUserRole(studentID) == userRole.STUDENT) {
            PreparedStatement ps = null;
            try {
                if (ds != null) {
                    if (con != null) {
                        String sql = "INSERT INTO grade(courseID, userID, grade) VALUES (?,?,?)";
                        ps = con.prepareStatement(sql);
                        ps.setLong(1, courseID);
                        ps.setLong(2, studentID);
                        ps.setDouble(3, grade);
                        ps.executeUpdate();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (ps != null) {
                        ps.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*
    returns the number of pending courses
     */
    public double getPendingTemplates(long courseID, int pendingStat)
    {
        double pendingCnt = 0;
        PreparedStatement ps;
        try {
            if (con != null) {
                String sql = "SELECT count(template.finished) as test  FROM template INNER JOIN course ON template.id=course.templateID AND course.courseID = (?) AND template.finished = (?)";
                ps = con.prepareStatement(sql);
                ps.setLong(1, courseID);
                ps.setInt(2, pendingStat);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    pendingCnt = rs.getInt(1);
                }
            }
        } catch (SQLException sqle) {
            System.out.println("Kann mich nicht verbinden");
            sqle.printStackTrace();
        }
        return pendingCnt;
    }
    public List<DatabaseElement> getUniversityList(long userID) {
        List<DatabaseElement> list = new ArrayList<DatabaseElement>();
        PreparedStatement ps = null;
        try {
            if (ds != null) {
                if (con != null) {
                    String sql = "SELECT university.id, university.name FROM university INNER JOIN userUniversity ON university.id=userUniversity.universityID AND userUniversity.UserID=(?)";
                    ps = con.prepareStatement(sql);
                    ps.setLong(1, userID);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        list.add(new DatabaseElement(rs.getLong("id"), rs.getString("name")));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public List<DatabaseElement> getAssigendStudentList(long courseID) {
        List<DatabaseElement> list = new ArrayList<DatabaseElement>();
        PreparedStatement ps = null;
        try {
            if (ds != null) {
                if (con != null) {
                    String sql = "SELECT user.id, user.firstname, user.lastname FROM user INNER JOIN courseStudent ON user.id=courseStudent.studentID AND courseStudent.courseID = (?)";
                    ps = con.prepareStatement(sql);
                    ps.setLong(1, courseID);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        list.add(new DatabaseElement(rs.getLong("id"), rs.getString("firstname"),rs.getString("lastname")));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }


    public List<DatabaseElement> getFachrichtungList(long userID, long universityID) {
        List<DatabaseElement> list = new ArrayList<DatabaseElement>();
        PreparedStatement ps = null;
        try {
            if (ds != null) {
                if (con != null) {
                    //List<DatabaseElement> courseList = getCourseList(userID, universityID,0);

                    //for(DatabaseElement element: courseList)
                    //{
                      //  String sql = "SELECT fachrichtung.* FROM fachrichtung INNER JOIN fachrichtungCourse ON fachrichtungCourse.idFachrichtung=fachrichtung.id INNER JOIN course ON fachrichtungCourse.idCourse = course.courseID AND course.courseID = (?)";
                        String sql = "SELECT fachrichtung.* FROM fachrichtung INNER JOIN userUniversity ON userUniversity.UniversityID = fachrichtung.universityID AND userUniversity.UserID = (?) AND userUniversity.UniversityID=(?)\n";
                        ps = con.prepareStatement(sql);
                        ps.setLong(1, userID);
                        ps.setLong(2, universityID);
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            boolean exists = false;
                            for(DatabaseElement listElement: list)
                            {
                                if(rs.getLong("id") == listElement.id)
                                {
                                    exists = true;
                                }
                            }
                            if(exists == false){
                                list.add(new DatabaseElement(rs.getLong("id"),rs.getString("name")));
                            }
                        }
                    //}
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }


    public List<DatabaseElement> getCourseList(long userID, long universityID, int pendingStat) {
        List<DatabaseElement> list = new ArrayList<DatabaseElement>();
        PreparedStatement ps = null;
        try {
            if (ds != null) {
                if (con != null) {
                    String sql = "SELECT course.* FROM course\n" +
                            "\t INNER JOIN university ON university.id=course.universityID AND university.id = (?)\n" +

                            "\t INNER JOIN staff_course_supervision ON staff_course_supervision.courseID=course.courseID AND staff_course_supervision.userID = (?) AND course.finished = 0";
                    ps = con.prepareStatement(sql);
                    ps.setLong(1, universityID);
                    ps.setLong(2, userID);

                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        list.add(new DatabaseElement(rs.getLong("courseID"),rs.getString("name")));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public List<DatabaseElement> getCourseListForFach(long userID, long universityID, long fachrichtungID, int pendingStat) {
        List<DatabaseElement> list = new ArrayList<DatabaseElement>();
        PreparedStatement ps = null;
        try {
            if (ds != null) {
                if (con != null) {
                    String sql = "SELECT course.* FROM course\n" +
                            "\t INNER JOIN university ON university.id=course.universityID AND university.id = (?)\n" +
                            "\t INNER JOIN fachrichtungCourse ON fachrichtungCourse.idCourse=course.courseID AND fachrichtungCourse.idFachrichtung =(?)\n"+
                            "\t INNER JOIN staff_course_supervision ON staff_course_supervision.courseID=course.courseID AND staff_course_supervision.userID = (?) AND course.finished = 0";
                    ps = con.prepareStatement(sql);
                    ps.setLong(1, universityID);

                    ps.setLong(2,fachrichtungID);
                    ps.setLong(3, userID);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        list.add(new DatabaseElement(rs.getLong("courseID"),rs.getString("name")));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }



    public List<DatabaseElement> getTemplateList(long courseID, int pendingStat) {
        List<DatabaseElement> list = new ArrayList<DatabaseElement>();
        PreparedStatement ps = null;
        try {
            if (ds != null) {
                if (con != null) {
                    String sql = "SELECT template.* FROM template INNER JOIN course ON template.id = course.templateID AND course.courseID = (?) AND template.finished = (?)";
                    ps = con.prepareStatement(sql);
                    ps.setLong(1, courseID);
                    ps.setInt(2, pendingStat);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        list.add(new DatabaseElement(rs.getLong("id"), rs.getString("name")));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public List<Double> getGrade(long studentID) {
        List<Double> list = new ArrayList<Double>();
        if (getUserRole(studentID) == userRole.STUDENT) {
            PreparedStatement ps = null;
            try {
                if (ds != null) {
                    if (con != null) {
                        String sql = "SELECT grade FROM grade WHERE userID = (?)";
                        ps = con.prepareStatement(sql);
                        ps.setLong(1, studentID);
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            list.add(rs.getDouble("grade"));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (ps != null) {
                        ps.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    public List<DatabaseElement> getSemesterList() {
        List<DatabaseElement> list = new ArrayList<DatabaseElement>();
        PreparedStatement ps = null;
        try {
            if (ds != null) {
                if (con != null) {
                    String sql = "SELECT * FROM semester";
                    ps = con.prepareStatement(sql);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        list.add(new DatabaseElement(rs.getLong("id"),rs.getString("name")));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public List<DatabaseElement> getFach(long studentID, long semesterID) {
        List<DatabaseElement> list = new ArrayList<DatabaseElement>();
        PreparedStatement ps = null;
        try {
            if (ds != null) {
                if (con != null) {
                    String sql = "SELECT course.* FROM course INNER JOIN courseStudent ON courseStudent.courseID= course.courseID AND courseStudent.studentID = (?) INNER JOIN semesterCourse ON semesterCourse.courseID = course.courseID AND semesterCourse.semesterID = (?)";
                    ps = con.prepareStatement(sql);
                    ps.setLong(1, studentID);
                    ps.setLong(2, semesterID);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        list.add(new DatabaseElement(rs.getLong("courseID"),rs.getString("name")));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public long addTemplate(String name, H2 h2, R2S r2s, S2G s2g){
        int id = -1;

        if (h2 != null && r2s != null && s2g != null) {
            PreparedStatement ps = null;
            try {
                if (ds != null) {
                    if (con != null) {
                        String sql = "INSERT INTO template(name, h2_ID, s2g_ID, r2s_ID) VALUES(?,?,?,?)";
                        ps = con.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
                        ps.setString(1, name);
                        ps.setLong(2, serializeObject(h2));
                        if (s2g.isSerialized) {
                            ps.setLong(3, s2g.getID());
                        } else {
                            ps.setLong(3, serializeObject(s2g));
                        }
                        ps.setLong(4, serializeObject(r2s));
                        ps.executeUpdate();

                        ResultSet rs = ps.getGeneratedKeys();
                        if (rs.next()) {
                            id = rs.getInt(1);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (ps != null) {
                        ps.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return id;
    }

    public long serializeObject(Object object) {
        int id = -1;

        if (object != null) {
            PreparedStatement ps = null;
            try {
                if (ds != null) {
                    if (con != null) {
                        String sql = "INSERT INTO data(name, object) VALUES(?,?)";
                        ps = con.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
                        ps.setString(1, object.getClass().getName());
                        ps.setObject(2, object);
                        ps.executeUpdate();

                        ResultSet rs = ps.getGeneratedKeys();
                        if (rs.next()) {
                            id = rs.getInt(1);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (ps != null) {
                        ps.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return id;
    }

    public Object deSerializeObject(long id) {
        Object object = new Object();

        PreparedStatement ps;
        ResultSet rs;
        try {
            if (con != null) {
                String sql = "SELECT object FROM data WHERE id = (?)";
                ps = con.prepareStatement(sql);
                ps.setLong(1, id);
                rs = ps.executeQuery();
                rs.next();
                // object = rs.getObject(1);

                byte[] buf = rs.getBytes(1);
                ObjectInputStream objectIn = null;
                if (buf != null)
                    objectIn = new ObjectInputStream(new ByteArrayInputStream(buf));

                object = objectIn.readObject();
            }
        } catch (SQLException sqle) {
            System.out.println("Kann mich nicht verbinden");
            sqle.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    public boolean addUser(String name, String password) {
        int i = 0;
        if (name != null) {
            PreparedStatement ps = null;
            try {
                if (ds != null) {
                    if (con != null) {
                        String sql = "INSERT INTO user(name, password) VALUES(?,?)";
                        ps = con.prepareStatement(sql);
                        ps.setString(1, name);
                        ps.setString(2, PasswordHash.createHash(password));
                        i = ps.executeUpdate();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (ps != null) {
                        ps.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        setUserRole(getUserID(name), getRoleID(userRole.DEFAULT));
        return i > 0;
    }

    public long getRoleID(userRole role) {
        long roleID = 0;
        PreparedStatement ps;
        try {
            if (con != null) {
                String sql = "SELECT roleID FROM userRole WHERE roleName = (?)";
                ps = con.prepareStatement(sql);
                ps.setString(1, role.toString().toLowerCase());

                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    roleID = rs.getInt(1);
                }
            }
        } catch (SQLException sqle) {
            System.out.println("Kann mich nicht verbinden");
            sqle.printStackTrace();
        }
        return roleID;
    }

    public void setUserRole(long userID, long roleID) {
        if (userID != 0) {
            PreparedStatement ps = null;
            try {
                if (ds != null) {
                    if (con != null) {
                        String sql = "INSERT INTO userUserRole(userID,roleID) VALUES (?,?)";
                        ps = con.prepareStatement(sql);
                        ps.setLong(1, userID);
                        ps.setLong(2, roleID);
                        ps.executeUpdate();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (ps != null) {
                        ps.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void changeUserRole(long userID, userRole role) {
        if (userID != 0) {
            PreparedStatement ps = null;
            try {
                if (ds != null) {
                    if (con != null) {
                        String sql = "DELETE from userUserRole where userID = '" + userID + "'";
                        ps = con.prepareStatement(sql);
                        ps.executeUpdate();
                        sql = "INSERT INTO userUserRole(userID,roleID) VALUES (?,?)";
                        ps = con.prepareStatement(sql);
                        long roleID = getRoleID(role);
                        ps.setLong(1, userID);
                        ps.setLong(2, roleID);
                        ps.executeUpdate();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (ps != null) {
                        ps.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public userRole getUserRole(long userID) {
        userRole role = null;

        PreparedStatement ps;

        ResultSet rs;
        try {
            if (con != null) {
                String sql = "select roleID from userUserRole where userID = '"
                        + userID + "'";
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                rs.next();
                int roleID = rs.getInt("roleID");
                sql = "select roleName from userRole where roleID = '" + roleID + "'";
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                rs.next();
                String s_roleID = rs.getString("roleName");
                for (userRole r : userRole.values()) {
                    if (r.toString().equalsIgnoreCase(s_roleID)) {
                        role = r;
                    }
                }
            }
        } catch (SQLException sqle) {
            System.out.println("Kann mich nicht verbinden");
            sqle.printStackTrace();
        }
        return role;
    }

    public int getUserID(String name) {
        int userID = 0;

        if (name != null) {
            PreparedStatement ps;
            if (ds != null) {
                try {
                    if (con != null) {
                        String sql = "select id from user where name = '"
                                + name + "'";
                        ps = con.prepareStatement(sql);
                        ResultSet rs = ps.executeQuery();
                        rs.next();
                        userID = rs.getInt("id");
                    }
                } catch (SQLException sqle) {
                    System.out.println("Kann mich nicht verbinden");
                    sqle.printStackTrace();
                }
            }
        }
        return userID;
    }

    public String getUserName(long userID) {
        String userName = null;

        if (userID != 0) {
            PreparedStatement ps;
            if (ds != null) {
                try {
                    if (con != null) {
                        String sql = "select name from user where id = '"
                                + userID + "'";
                        ps = con.prepareStatement(sql);
                        ResultSet rs = ps.executeQuery();
                        rs.next();
                        userName = rs.getString("name");
                    }
                } catch (SQLException sqle) {
                    System.out.println("Kann mich nicht verbinden");
                    sqle.printStackTrace();
                }
            }
        }
        return userName;
    }

    public String getPasswordHash(long userID) {
        String passwordHash = null;

        if (userID != 0) {
            PreparedStatement ps;
            if (ds != null) {
                try {
                    if (con != null) {
                        String sql = "select password from user where id = '"
                                + userID + "'";
                        ps = con.prepareStatement(sql);
                        ResultSet rs = ps.executeQuery();
                        rs.next();
                        passwordHash = rs.getString("password");
                    }
                } catch (SQLException sqle) {
                    System.out.println("Kann mich nicht verbinden");
                    sqle.printStackTrace();
                }
            }
        }
        return passwordHash;
    }

    public String getFirstName(long userID) {
        String firstName = null;

        PreparedStatement ps;

        ResultSet rs;
        try {
            if (con != null) {
                String sql = "SELECT firstname FROM user WHERE id = (?)";
                ps = con.prepareStatement(sql);
                ps.setLong(1, userID);
                rs = ps.executeQuery();

                if (rs.next()) {
                    firstName = rs.getString(1);
                }
            }
        } catch (SQLException sqle) {
            System.out.println("Kann mich nicht verbinden");
            sqle.printStackTrace();
        }
        return firstName;
    }

    public String getLastName(long userID) {
        String lastName = null;

        PreparedStatement ps;

        ResultSet rs;
        try {
            if (con != null) {
                String sql = "SELECT lastname FROM user WHERE id = (?)";
                ps = con.prepareStatement(sql);
                ps.setLong(1, userID);
                rs = ps.executeQuery();

                if (rs.next()) {
                    lastName = rs.getString(1);
                }
            }
        } catch (SQLException sqle) {
            System.out.println("Kann mich nicht verbinden");
            sqle.printStackTrace();
        }
        return lastName;
    }

    public void close() {
        try {
            if (con != null) {
                con.close();
            } else
                throw new Exception("Error with Connection");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static enum userRole {
        ADMIN, TUTOR, STUDENT, ACCESSOR, DEFAULT
    }

}
