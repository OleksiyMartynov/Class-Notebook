package models.database;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

abstract class MyDatabase extends SQLiteOpenHelper {
    private Context context;
    private SQLiteDatabase db;
    private String name;
    private int ver;
    private String[] onCreate, onUpgrade;

    protected MyDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, String[] createQuery, String[] upgradeQuery) {
        super(context, name, factory, version);
        this.context = context;
        this.name = name;
        this.ver = version;
        this.onCreate = createQuery;
        this.onUpgrade = upgradeQuery;
        db = this.getWritableDatabase();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (int i = 0; i < onCreate.length; i++) {
            db.execSQL(this.onCreate[i]);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        for (int i = 0; i < onUpgrade.length; i++) {
            db.execSQL(this.onUpgrade[i]);
        }

    }
    public List<List<String>> executeQuery(String query)
    {
        Log.i("database","query will run:"+query);
        Cursor c =this.db.rawQuery(query,null);
        int colCount = c.getColumnCount();
        Log.i("db","col count :"+ colCount);
        List<List<String>> rowsArr = new ArrayList();
        if(c.moveToFirst()) {


            Log.i("database", "first:" + c.getString(0));

            do {
                List<String> rowDataArr = new ArrayList();
                for (int i = 0; i < colCount; i++) {
                    rowDataArr.add(c.getString(i));
                }
                rowsArr.add(rowDataArr);
            }while (c.moveToNext());
        }
        c.close();
        return rowsArr;
    }

    public List<List<Object>> executeQuerySmart(String query)
    {
        Log.i("database", "query will run:" + query);
        Cursor c = this.db.rawQuery(query, null);
        int colCount = c.getColumnCount();
        List<List<Object>> rowsArr = new ArrayList();
        if (c.moveToFirst())
        {
            do
            {
                List<Object> rowDataArr = new ArrayList();
                for (int i = 0; i < colCount; i++)
                {
                    switch (c.getType(i))
                    {
                        case Cursor.FIELD_TYPE_BLOB:
                        {

                            byte[] bytes = c.getBlob(i);
                            Byte[] byteObjects = new Byte[bytes.length];
                            int index = 0;
                            for (byte b : bytes)
                            {
                                byteObjects[index++] = b;
                            }
                            rowDataArr.add(byteObjects);
                            break;
                        }
                        case Cursor.FIELD_TYPE_FLOAT:
                        {
                            rowDataArr.add(Float.valueOf(c.getFloat(i)));
                            break;
                        }
                        case Cursor.FIELD_TYPE_INTEGER:
                        {
                            rowDataArr.add(Integer.valueOf(c.getInt(i)));
                            break;
                        }
                        case Cursor.FIELD_TYPE_NULL:
                        {
                            rowDataArr.add(c.getString(i));
                            break;
                        }
                        case Cursor.FIELD_TYPE_STRING:
                        {
                            rowDataArr.add(c.getString(i));
                            break;
                        }
                    }

                }
                rowsArr.add(rowDataArr);
            } while (c.moveToNext());
        }
        c.close();
        return rowsArr;
    }
}