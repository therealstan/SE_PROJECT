/**
 * Created by Dom on 27.04.2015.
 */
public class DatabaseElement {

    long id;
    public long getId()
    {
     return id;
    }

    String name;

    public DatabaseElement (long id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }
}
