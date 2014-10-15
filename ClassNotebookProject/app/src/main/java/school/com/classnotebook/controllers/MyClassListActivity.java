package school.com.classnotebook.controllers;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.List;

import models.containers.MyClassData;
import models.database.MyAppDatabase;
import school.com.classnotebook.R;


public class MyClassListActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_class_list);

        //test
        /*
        MyAppDatabase classDb = MyAppDatabase.getInstance(this);
        classDb.saveClassData(new MyClassData("math", "bob", "1/2/2014"));
        classDb.saveClassData(new MyClassData("english", "john", "1/3/2014"));
        List<MyClassData> classes = classDb.getClassList();
        for (MyClassData cd : classes)
        {
            Log.i("test", cd.toString());
        }
        int fk = 1;
        classDb.saveNoteData(new MyNoteData(MyNoteData.Type.text.toString(), "calc 1", "1/7/2014", fk));
        classDb.saveNoteData(new MyNoteData(MyNoteData.Type.text.toString(), "calc 2", "1/12/2014", fk));
        List<MyNoteData> notes = classDb.getNoteList(fk);
        for (MyNoteData nd : notes)
        {
            Log.i("test", nd.toString());
        }
        */
        prepareList(MyAppDatabase.getInstance(this).getClassList());
    }

    private void prepareList(List<MyClassData> data)
    {
        ListView listView = (ListView) findViewById(R.id.classListView);
        MyClassListAdapter adapter = new MyClassListAdapter(this, R.layout.class_list_cell, data);
        listView.setAdapter(adapter);
        //on data changes call adapter.notifyDataSetChanged();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_class_list, menu);
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
