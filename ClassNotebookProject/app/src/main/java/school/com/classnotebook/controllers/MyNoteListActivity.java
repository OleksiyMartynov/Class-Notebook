package school.com.classnotebook.controllers;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import school.com.classnotebook.R;


public class MyNoteListActivity extends ActionBarActivity
{
    public static String NOTE_PARENT_ID_KEY = "note_parent_id";
    private int noteFkId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        noteFkId = getIntent().getIntExtra(MyNoteListActivity.NOTE_PARENT_ID_KEY, -1);
        Log.i("MyNoteListActivity", "get notes for class with id:" + noteFkId);
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
