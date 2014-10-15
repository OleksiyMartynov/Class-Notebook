package models.containers;

import java.util.Date;

/**
 * Created by Oleksiy on 10/14/2014.
 */
public class MyNoteData
{
    private int id =-1;
    private int fk_id = -1;
    private Type typeOfData;
    private Date date;

    public MyNoteData(int id, Type typeOfData, Date date) {
        this.id = id;
        this.typeOfData = typeOfData;
        this.date = date;
    }

    public MyNoteData(Type typeOfData, Date date) {
        this.typeOfData = typeOfData;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public Type getTypeOfData() {
        return typeOfData;
    }

    public Date getDate() {
        return date;
    }

    public enum Type {text, image, audio, drawing}
}

