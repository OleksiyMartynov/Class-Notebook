package models.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
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
    public static final int NOTEBOOK_DATABASE_VERSION = 3;
    private static String CREATE_CLASS_TABLE = "CREATE TABLE IF NOT EXISTS " + Tables.class_table + " (" + ClassTableColumns.class_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ClassTableColumns.class_name + " TEXT, " + ClassTableColumns.class_proff + " TEXT, " + ClassTableColumns.class_date + " TEXT);";
    ;
    private static String CREATE_NOTE_TABLE = "CREATE TABLE IF NOT EXISTS " + Tables.note_table + " (" + NoteTableColumns.note_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NoteTableColumns.note_type + " TEXT, " + NoteTableColumns.note_name + " TEXT, " + NoteTableColumns.note_date + " TEXT, " + NoteTableColumns.note_class_fk + " INTEGER," + NoteTableColumns.note_data + " BLOB" + ");";
    ;
    private static String UPGRADE_CLASS_TABLE = "DROP TABLE IF EXISTS " + Tables.class_table + "; ";
    ;
    private static String UPGRADE_NOTE_TABLE = "DROP TABLE IF EXISTS " + Tables.note_table + "; ";
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

    public boolean updateClassData(MyClassData classData)
    {
        String updateQuery = "UPDATE " + Tables.class_table + " SET " + ClassTableColumns.class_name + " = " + "'" + classData.getName() + "'" + ", " + ClassTableColumns.class_proff + " = " + "'" + classData.getProff() + "'" + ", " + ClassTableColumns.class_date + " = " + "'" + classData.getDate() + "'" + " WHERE " + ClassTableColumns.class_id + " = " + classData.getId() + ";";
        try
        {
            executeQuery(updateQuery);
            return false;
        } catch (Exception e)
        {
            return true;
        }
    }

    public boolean updateNoteData(MyNoteData noteData)
    {
        String updateQuery = "UPDATE " + Tables.note_table + " SET " + NoteTableColumns.note_name + " = " + "'" + noteData.getName() + "'" + ", " + NoteTableColumns.note_date + " = " + "'" + noteData.getDate() + "'" + " WHERE " + NoteTableColumns.note_id + " = " + noteData.getId() + ";";
        try
        {
            executeQuery(updateQuery);
            return false;
        } catch (Exception e)
        {
            return true;
        }
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

    public MyClassData getClassData(int id)
    {
        String select = "SELECT * FROM " + Tables.class_table + " WHERE " + ClassTableColumns.class_id + " = " + id + " ;";
        List<List<String>> results = executeQuery(select);
        for (List<String> row : results)
        {
            return new MyClassData(Integer.valueOf(row.get(0)), row.get(1), row.get(2), row.get(3));
        }
        return null;
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

    public MyNoteData getNoteData(int id)
    {
        String select = "SELECT * FROM " + Tables.note_table + " WHERE " + NoteTableColumns.note_id + " = " + id + " ;";
        List<List<String>> results = executeQuery(select);
        for (List<String> row : results)
        {
            return new MyNoteData(Integer.valueOf(row.get(0)), row.get(1), row.get(3), row.get(2), Integer.valueOf(row.get(4)));
        }
        return null;
    }

    public List<MyNoteData> getNoteList(int classId)
    {
        String selectAll = "SELECT * FROM " + Tables.note_table + " WHERE " + NoteTableColumns.note_class_fk + "=" + classId + ";";
        List<List<String>> results = executeQuery(selectAll);
        List<MyNoteData> data = new ArrayList<MyNoteData>();
        for (List<String> row : results)
        {
            MyNoteData n = new MyNoteData(Integer.valueOf(row.get(0)), row.get(1), row.get(3), row.get(2), Integer.valueOf(row.get(4)));
            data.add(n);
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
        String insertQuery = "INSERT INTO " + Tables.note_table + " VALUES(NULL,'" + data.getTypeOfData() + "','" + data.getName() + "','" + data.getDate() + "'," + data.getFk_id() + ");";
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

    public boolean deleteClassData(List<MyClassData> data)
    {
        boolean flag = true;
        for (MyClassData item : data)
        {
            if (!deleteClassData(item))
            {
                flag = false;
            }
        }
        return flag;
    }

    public boolean deleteNoteData(List<MyNoteData> data)
    {
        boolean flag = true;
        for (MyNoteData item : data)
        {
            if (!deleteNoteData(item))
            {
                flag = false;
            }
        }
        return flag;
    }

    public boolean deleteClassData(MyClassData data)
    {
        String deleteQuery = "DELETE FROM " + Tables.class_table + " WHERE " + ClassTableColumns.class_id + " = " + data.getId() + ";";
        try
        {
            deleteNoteDataFromClass(data);//delete existing notes with same fk id and class id
            executeQuery(deleteQuery);
            return true;
        } catch (Exception e)
        {
            return false;
        }
    }

    public boolean deleteNoteDataFromClass(MyClassData classData)
    {
        String deleteQuery = "DELETE FROM " + Tables.note_table + " WHERE " + NoteTableColumns.note_class_fk + " = " + classData.getId() + ";";
        try
        {
            executeQuery(deleteQuery);
            return true;
        } catch (Exception e)
        {
            return false;
        }
    }

    public boolean deleteNoteData(MyNoteData data)
    {
        return deleteNoteData(data.getId());
    }

    private boolean deleteNoteData(int id)
    {
        String deleteQuery = "DELETE FROM " + Tables.note_table + " WHERE " + NoteTableColumns.note_id + " = " + id + ";";
        try
        {
            executeQuery(deleteQuery);
            return true;
        } catch (Exception e)
        {
            return false;
        }
    }

    public void saveNoteDataSmart(MyNoteData data)
    {
        String insertQuery = "INSERT INTO " + Tables.note_table + "( " + NoteTableColumns.note_id + "," + NoteTableColumns.note_type + "," + NoteTableColumns.note_name + "," + NoteTableColumns.note_date + "," + NoteTableColumns.note_class_fk + "," + NoteTableColumns.note_data + ")" + " VALUES(?,?,?,?,?,? );";
        SQLiteDatabase db = getWritableDatabase();
        SQLiteStatement statement = db.compileStatement(insertQuery);
        statement.clearBindings();
        statement.bindNull(1);
        statement.bindString(2, data.getTypeOfData());
        statement.bindString(3, data.getName());
        statement.bindString(4, data.getDate());
        statement.bindString(5, Integer.toString(data.getFk_id()));
        statement.bindBlob(6, data.getData());
        statement.executeInsert();
        //db.close();
    }

    public void updateNoteDataSmart(MyNoteData data)
    {
        String insertQuery = "UPDATE " + Tables.note_table + " SET " + NoteTableColumns.note_name + " = ?," + NoteTableColumns.note_date + " =?," + NoteTableColumns.note_data + " =? WHERE " + NoteTableColumns.note_id + " =?;";
        SQLiteDatabase db = getWritableDatabase();
        SQLiteStatement statement = db.compileStatement(insertQuery);
        statement.clearBindings();
        statement.bindString(1, data.getName());
        statement.bindString(2, data.getDate());
        statement.bindBlob(3, data.getData());
        statement.bindString(4, Integer.toString(data.getId()));
        statement.executeInsert();
        //db.close();
    }

    public MyNoteData getNoteDataSmart(int id)
    {
        String select = "SELECT * FROM " + Tables.note_table + " WHERE " + NoteTableColumns.note_id + " = " + id + " ;";
        List<List<Object>> results = executeQuerySmart(select);
        for (List<Object> row : results)
        {
            return new MyNoteData(((Integer) row.get(0)).intValue(), (String) row.get(1), (String) row.get(3), (String) row.get(2), ((Integer) row.get(4)).intValue(), byteObjToPrimitives((Byte[]) row.get(5)));
        }
        return null;
    }

    public List<MyNoteData> getNoteListSmart(int classId)
    {
        String select = "SELECT * FROM " + Tables.note_table + " WHERE " + NoteTableColumns.note_class_fk + "=" + classId + ";";
        List<List<Object>> results = executeQuerySmart(select);
        List<MyNoteData> data = new ArrayList<MyNoteData>();
        for (List<Object> row : results)
        {
            try
            {
                MyNoteData n = new MyNoteData(((Integer) row.get(0)).intValue(), (String) row.get(1), (String) row.get(3), (String) row.get(2), ((Integer) row.get(4)).intValue(), byteObjToPrimitives((Byte[]) row.get(5)));
                data.add(n);
            } catch (Exception e)
            {
                Log.i("AppDatabase", "malformed data found in row");
            }
        }
        return data;
    }

    private byte[] byteObjToPrimitives(Byte[] b)
    {
        byte[] out = new byte[b.length];
        int i = 0;
        for (Byte by : b)
        {
            out[i++] = by;
        }
        return out;
    }

    private enum Tables
    {
        class_table, note_table
    }

    private enum ClassTableColumns
    {
        class_id, class_name, class_proff, class_date
    }

    private enum NoteTableColumns
    {
        note_id, note_date, note_name, note_type, note_class_fk, note_data
    }
}
