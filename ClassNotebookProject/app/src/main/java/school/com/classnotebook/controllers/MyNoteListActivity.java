package school.com.classnotebook.controllers;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import models.containers.MyClassData;
import models.containers.MyNoteData;
import models.database.MyAppDatabase;
import school.com.classnotebook.R;


public class MyNoteListActivity extends ActionBarActivity
{
    public static String CLASS_ID = "CLASS_id";
    private int classId;
    private MyNoteListAdapter adapter;
    private List<MyNoteData> data;
    private List<Integer> selectedItems = new ArrayList<Integer>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        classId = getIntent().getIntExtra(MyNoteListActivity.CLASS_ID, -1);
        Log.i("MyNoteListActivity", "get notes for class with id:" + classId);
        if (classId != -1)
        {
            MyClassData data = MyAppDatabase.getInstance(this).getClassData(classId);
            setTitles(data);
            prepareList(data);
        }
    }

    private void setTitles(MyClassData data)
    {
        setTitle(data.getName());
        ((TextView) findViewById(R.id.noteClassProff)).setText(data.getProff());
        ((TextView) findViewById(R.id.noteClassDate)).setText(data.getDate());
    }

    private void prepareList(MyClassData data)
    {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.note_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}
