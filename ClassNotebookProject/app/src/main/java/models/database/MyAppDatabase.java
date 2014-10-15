package models.database;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import models.containers.MyClassData;
import models.containers.MyNoteData;

/**
 * Created by Oleksiy on 10/14/2014.
 */
public class MyAppDatabase extends MyDatabase
{
    public static final int NOTEBOOK_DATABASE_VERSION = 1;
    private static String CREATE_CLASS_TABLE = "CREATE TABLE IF NOT EXISTS " + Tables.class_table + " (" + ClassTableColumns.class_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ClassTableColumns.class_name + " TEXT, " + ClassTableColumns.class_proff + " TEXT, " + ClassTableColumns.class_date + " TEXT);";
    ;
    private static String CREATE_NOTE_TABLE = "CREATE TABLE IF NOT EXISTS " + Tables.note_table + " (" + NoteTableColumns.note_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NoteTableColumns.note_type + " TEXT, " + NoteTableColumns.note_name + " TEXT, " + NoteTableColumns.note_date + " TEXT, " + NoteTableColumns.note_class_fk + " INTEGER);";
    ;
    private static String UPGRADE_CLASS_TABLE = "DROP TABLE IF EXISTS " + Tables.class_table + ";";
    ;
    private static String UPGRADE_NOTE_TABLE = "DROP TABLE IF EXISTS " + Tables.note_table + ";";
    private static MyAppDatabase instance;

    private MyAppDatabase(Context context)
    {
        super(context, "class_notebook_app_database", null, NOTEBOOK_DATABASE_VERSION, new String[]{CREATE_CLASS_TABLE, CREATE_NOTE_TABLE}, new String[]{UPGRADE_CLASS_TABLE, UPGRADE_NOTE_TABLE});
    }

    public static MyAppDatabase getInstance(Context context)
    {
        instance = instance == null ? new MyAppDatabase(context) : instance;
        return instance;
    }

    public List<MyClassData> getClassList()
    {
        String selectAll = "SELECT * FROM " + Tables.class_table + ";";
        List<List<String>> results = executeQuery(selectAll);
        List<MyClassData> data = new ArrayList<MyClassData>();
        for (List<String> row : results)
        {
            data.add(new MyClassData(Integer.valueOf(row.get(0)), row.get(1), row.get(2), row.get(3)));
        }
        return data;
    }

    public List<MyNoteData> getNoteList()
    {
        String selectAll = "SELECT * FROM " + Tables.note_table + ";";
        List<List<String>> results = executeQuery(selectAll);
        List<MyNoteData> data = new ArrayList<MyNoteData>();
        for (List<String> row : results)
        {
            data.add(new MyNoteData(Integer.valueOf(row.get(0)), row.get(1), row.get(2), row.get(3)));
        }
        return data;
    }

    public List<MyNoteData> getNoteList(int classId)
    {
        String selectAll = "SELECT * FROM " + Tables.note_table + " WHERE " + NoteTableColumns.note_class_fk + "=" + classId + ";";
        List<List<String>> results = executeQuery(selectAll);
        List<MyNoteData> data = new ArrayList<MyNoteData>();
        for (List<String> row : results)
        {
            data.add(new MyNoteData(Integer.valueOf(row.get(0)), row.get(1), row.get(2), row.get(3), Integer.valueOf(row.get(4))));
        }
        return data;
    }

    public boolean saveClassData(MyClassData data)
    {
        String insertQuery = "INSERT INTO " + Tables.class_table + " VALUES(NULL,'" + data.getName() + "','" + data.getProff() + "','" + data.getDate() + "');";
        try
        {
            executeQuery(insertQuery);
            return true;
        } catch (Exception e)
        {
            Log.w("MyAppDatabase", "insert of class_data failed");
            return false;
        }
    }

    public boolean saveNoteData(MyNoteData data)
    {
        String insertQuery = "INSERT INTO " + Tables.note_table + " VALUES(NULL,'" + data.getName() + "','" + data.getDate() + "','" + data.getTypeOfData() + "'," + data.getFk_id() + ");";
        try
        {
            executeQuery(insertQuery);
            return true;
        } catch (Exception e)
        {
            Log.w("MyAppDatabase", "insert of note_data failed");
            return false;
        }
    }
    private enum Tables {class_table, note_table}

    private enum ClassTableColumns {class_id, class_name, class_proff, class_date}

    private enum NoteTableColumns {note_id, note_date, note_name, note_type, note_class_fk}
}
