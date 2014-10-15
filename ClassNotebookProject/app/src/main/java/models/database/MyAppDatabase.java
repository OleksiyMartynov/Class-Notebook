package models.database;

import android.content.Context;

/**
 * Created by Oleksiy on 10/14/2014.
 */
public class MyAppDatabase extends MyDatabase {
    public static final int NOTEBOOK_DATABASE_VERSION = 1;
    private static String CREATE_CLASS_TABLE = "CREATE TABLE IF NOT EXISTS " + Tables.class_table + " (" + ClassTableColumns.class_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ClassTableColumns.class_name + " TEXT, " + ClassTableColumns.class_proff + " TEXT, " + ClassTableColumns.class_date + " TEXT);";
    ;
    private static String CREATE_NOTE_TABLE = "CREATE TABLE IF NOT EXISTS " + Tables.note_table + " (" + NoteTableColumns.note_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NoteTableColumns.note_name + " TEXT, " + NoteTableColumns.note_date + " TEXT, " + NoteTableColumns.note_type + " TEXT, " + NoteTableColumns.note_class_fk + " INTEGER);";
    ;
    private static String UPGRADE_CLASS_TABLE = "DROP TABLE IF EXISTS " + Tables.class_table + ";";
    ;
    private static String UPGRADE_NOTE_TABLE = "DROP TABLE IF EXISTS " + Tables.note_table + ";";
    private MyAppDatabase instance;

    private MyAppDatabase(Context context) {
        super(context, "class_notebook_app_database", null, NOTEBOOK_DATABASE_VERSION, new String[]{CREATE_CLASS_TABLE, CREATE_NOTE_TABLE}, new String[]{UPGRADE_CLASS_TABLE, UPGRADE_NOTE_TABLE});
    }

    public MyAppDatabase getInstance(Context context) {
        instance = instance == null ? new MyAppDatabase(context) : instance;
        return instance;
    }

    private enum Tables {class_table, note_table}

    private enum ClassTableColumns {class_id, class_name, class_proff, class_date}

    private enum NoteTableColumns {note_id, note_date, note_name, note_type, note_class_fk}
}
