package school.com.classnotebook.controllers;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import models.containers.MyClassData;
import models.database.MyAppDatabase;
import school.com.classnotebook.R;


public class MyClassListActivity extends ActionBarActivity implements MyDialogListener
{
    private MyClassListAdapter adapter;
    private List<MyClassData> data;
    private List<Integer> selectedItems = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_class_list);

        prepareList();
    }

    private void prepareList()
    {
        data = MyAppDatabase.getInstance(this).getClassList();
        ListView listView = (ListView) findViewById(R.id.classListView);
        adapter = new MyClassListAdapter(this, R.layout.class_list_cell, data);
        listView.setLongClickable(true);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Intent intent = new Intent(MyClassListActivity.this, MyNoteListActivity.class);
                intent.putExtra(MyNoteListActivity.CLASS_ID, data.get(i).getId());
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
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_class_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id)
        {
            case R.id.action_add:
            {
                DialogFragment newFragment = new MyClassDialog();
                newFragment.show(getSupportFragmentManager(), "new_class");
                break;
            }
            case R.id.action_delete:
            {
                if (selectedItems.size() == 0)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("No classes selected for deletion! Long-press to select a class for deletion.")
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
                    MyAppDatabase.getInstance(this).deleteClassData(data.get(i));
                    data.remove(i.intValue());
                    adapter.notifyDataSetChanged();
                    ListView lv = ((ListView) findViewById(R.id.classListView));
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
            case R.id.action_info:
            {
                Log.i("MyClassListActivity", "info clicked");//todo create info activity
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void refresh()
    {
        data.clear();
        data.addAll(MyAppDatabase.getInstance(this).getClassList());
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
        refresh();
    }

    @Override
    public void onNegativeButtonClicked()
    {

    }
}
