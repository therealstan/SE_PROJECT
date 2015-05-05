/**
 * Created by Dom on 27.04.2015.
 */
public class DatabaseElement {


    long id;


    String name;
    String firstname;
    String lastname;

    public DatabaseElement (long id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public DatabaseElement(long id, String firstname,String lastname)
    {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public long getId()
    {
        return id;
    }

    public String getName()
    {
        return this.name;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }
}
