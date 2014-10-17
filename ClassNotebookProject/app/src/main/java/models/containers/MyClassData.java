package models.containers;

/**
 * Created by Oleksiy on 10/14/2014.
 */
public class MyClassData
{
    private int id = -1;
    private String name, proff;
    private String date;

    public MyClassData(int id, String name, String proff, String date)
    {
        this.id = id;
        this.name = name;
        this.proff = proff;
        this.date = date;
    }

    public MyClassData(String name, String proff, String date)
    {
        this.name = name;
        this.proff = proff;
        this.date = date;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getProff()
    {
        return proff;
    }

    public void setProff(String proff)
    {
        this.proff = proff;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    @Override
    public String toString()
    {
        return "MyClassData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", proff='" + proff + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
