package models.containers;

import java.util.Date;

/**
 * Created by Oleksiy on 10/14/2014.
 */
public class MyClassData
{
    private int id=-1;
    private String name, proff;
    private Date date;

    public MyClassData(int id, String name, String proff, Date date) {
        this.id = id;
        this.name = name;
        this.proff = proff;
        this.date = date;
    }

    public MyClassData(String name, String proff, Date date) {
        this.name = name;
        this.proff = proff;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProff() {
        return proff;
    }

    public void setProff(String proff) {
        this.proff = proff;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
