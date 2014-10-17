package school.com.classnotebook.controllers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import models.containers.MyClassData;
import models.containers.MyNoteData;
import models.database.MyAppDatabase;
import school.com.classnotebook.R;


public class MyNoteListActivity extends ActionBarActivity implements MyDialogListener
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
            MyClassData d = MyAppDatabase.getInstance(this).getClassData(classId);
            setTitles(d);
            prepareList(d);
        }
    }

    private void setTitles(MyClassData data)
    {
        setTitle(data.getName());
        ((TextView) findViewById(R.id.noteClassProff)).setText(data.getProff());
        ((TextView) findViewById(R.id.noteClassDate)).setText(data.getDate());
    }

    private void prepareList(MyClassData d)
    {
        data = MyAppDatabase.getInstance(this).getNoteListSmart(d.getId());
        ListView listView = (ListView) findViewById(R.id.notesListView);
        adapter = new MyNoteListAdapter(this, R.layout.note_list_cell, data);
        listView.setLongClickable(true);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Intent intent = new Intent(MyNoteListActivity.this, MyNoteActivity.class);
                intent.putExtra(MyNoteActivity.NOTE_TYPE, data.get(i).getTypeOfData());
                intent.putExtra(MyNoteActivity.CLASS_ID, classId);
                intent.putExtra(MyNoteActivity.NOTE_ID, data.get(i).getId());
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                if (view.getBackground() != null && ((ColorDrawable) view.getBackground()).getColor() != Color.TRANSPARENT)
                {
                    view.setBackgroundColor(Color.TRANSPARENT);
                    selectedItems.remove(Integer.valueOf(i));
                } else
                {
                    view.setBackgroundColor(Color.GRAY);
                    selectedItems.add(Integer.valueOf(i));
                }
                return true;
            }
        });
        //on data changes call adapter.notifyDataSetChanged();
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
        switch (id)
        {
            case R.id.action_edit_class:
            {
                MyClassDialog newFragment = new MyClassDialog();
                newFragment.setClassData(MyAppDatabase.getInstance(this).getClassData(classId));
                newFragment.show(getSupportFragmentManager(), "edit_class");
                break;
            }
            case R.id.action_add_note:
            {
                MyNoteDialog newFragment = new MyNoteDialog();
                newFragment.setClassId(classId);
                newFragment.setListener(new MyDialogListener()
                {
                    @Override
                    public void onPositiveButtonClicked()
                    {
                        refresh();
                    }

                    @Override
                    public void onNegativeButtonClicked()
                    {

                    }
                });
                newFragment.show(getSupportFragmentManager(), "new_note");
                break;
            }
            case R.id.action_delete_note:
            {
                if (selectedItems.size() == 0)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("No notes selected for deletion! Long-press to select a note for deletion.")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int id)
                                {
                                    //do things
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                for (Integer i : selectedItems)
                {
                    MyAppDatabase.getInstance(this).deleteNoteData(data.get(i));
                    data.remove(i.intValue());
                    adapter.notifyDataSetChanged();
                    ListView lv = ((ListView) findViewById(R.id.notesListView));
                    int itemsCount = lv.getChildCount();
                    for (int index = 0; index < itemsCount; index++)
                    {
                        View v = lv.getChildAt(index);
                        if (v != null)
                        {
                            v.setBackgroundColor(Color.TRANSPARENT);
                        }
                    }
                }
                selectedItems.clear();
                break;
            }
            case R.id.action_info_note:
            {
                //todo create info page for notes
                Log.i("MyNoteListActivity", "on info clicked");
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void refresh()
    {
        data.clear();
        data.addAll(MyAppDatabase.getInstance(this).getNoteListSmart(classId));
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        refresh();
    }

    @Override
    public void onPositiveButtonClicked()
    {
        MyClassData data = MyAppDatabase.getInstance(this).getClassData(classId);
        setTitles(data);
    }

    @Override
    public void onNegativeButtonClicked()
    {

    }
}
