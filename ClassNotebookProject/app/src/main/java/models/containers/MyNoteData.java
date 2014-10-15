package models.containers;

/**
 * Created by Oleksiy on 10/14/2014.
 */
public class MyNoteData
{
    private int id =-1;
    private int fk_id = -1;
    private String typeOfData;
    private String date;
    private String name;

    public MyNoteData(int id, String typeOfData, String name, String date)
    {
        this.id = id;
        this.typeOfData = typeOfData;
        this.date = date;
        this.name = name;
    }

    public MyNoteData(String typeOfData, String name, String date)
    {
        this.typeOfData = typeOfData;
        this.date = date;
        this.name = name;
    }

    public MyNoteData(String typeOfData, String date, String name, int fk_id)
    {
        this.typeOfData = typeOfData;
        this.date = date;
        this.name = name;
        this.fk_id = fk_id;
    }

    public MyNoteData(int id, String typeOfData, String date, String name, int fk_id)
    {
        this.id = id;
        this.fk_id = fk_id;
        this.typeOfData = typeOfData;
        this.date = date;
        this.name = name;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public int getFk_id()
    {
        return fk_id;
    }

    @Override
    public String toString()
    {
        return "MyNoteData{" +
                "id=" + id +
                ", fk_id=" + fk_id +
                ", typeOfData='" + typeOfData + '\'' +
                ", date='" + date + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getTypeOfData()
    {
        return typeOfData;
    }

    public String getDate()
    {
        return date;
    }

    public enum Type {text, image, audio, drawing}
}

