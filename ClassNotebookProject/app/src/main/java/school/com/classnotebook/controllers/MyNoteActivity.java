package school.com.classnotebook.controllers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import models.containers.MyNoteData;
import models.database.MyAppDatabase;
import school.com.classnotebook.R;


public class MyNoteActivity extends ActionBarActivity
{
    public static String NOTE_TYPE = "note_type";
    public static String CLASS_ID = "class_id";
    public static String NOTE_ID = "note_id";
    private int classId, noteId;
    private String noteType;
    private MyNoteFragmentProtocols mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_note);

        classId = getIntent().getIntExtra(MyNoteActivity.CLASS_ID, -1);
        noteType = getIntent().getStringExtra(MyNoteActivity.NOTE_TYPE);
        noteId = getIntent().getIntExtra(MyNoteActivity.NOTE_ID, -1);
        if (classId != -1 && noteType != null)
        {
            if (savedInstanceState == null)
            {
                ((EditText) findViewById(R.id.noteTitleEditText)).setHint("Title");
                Bundle bundle = new Bundle();
                bundle.putInt(MyTextNoteFragment.CLASS_ID_KEY, classId);
                if (noteType.equals(MyNoteData.Type.text.toString()))
                {
                    setTitle("Text Note");
                    MyTextNoteFragment f = new MyTextNoteFragment();
                    if (noteId != -1)
                    {
                        MyNoteData n = MyAppDatabase.getInstance(this).getNoteDataSmart(noteId);
                        f.setNoteData(n.getData());
                        ((EditText) findViewById(R.id.noteTitleEditText)).setText(n.getName());
                    }
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.container, f)
                            .commit();
                    mainFragment = f;
                } else if (noteType.equals(MyNoteData.Type.audio.toString()))
                {

                } else if (noteType.equals(MyNoteData.Type.image.toString()))
                {

                } else if (noteType.equals(MyNoteData.Type.drawing.toString()))
                {

                }
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_note, menu);
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
            case R.id.action_save_note:
            {
                if (mainFragment != null)
                {
                    String noteName;
                    if (((EditText) findViewById(R.id.noteTitleEditText)).getText().toString() == null || ((EditText) findViewById(R.id.noteTitleEditText)).getText().toString().isEmpty())
                    {
                        noteName = noteType + " note";
                    } else
                    {
                        noteName = ((EditText) findViewById(R.id.noteTitleEditText)).getText().toString();
                    }
                    if (noteId == -1) //new note
                    {
                        MyAppDatabase.getInstance(this).saveNoteDataSmart(new MyNoteData(MyNoteData.Type.text.toString(), dateAsString(), noteName, classId, mainFragment.getNoteData()));
                    } else //editing existing
                    {
                        MyAppDatabase.getInstance(this).updateNoteDataSmart(new MyNoteData(noteId, MyNoteData.Type.text.toString(), dateAsString(), noteName, classId, mainFragment.getNoteData()));
                    }
                }
                finish();
                break;
            }
            case R.id.action_cancel_note:
            {
                finish();
                break;
            }
            case R.id.action_info_type_note:
            {
                //todo show info for note of specific type
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private String dateAsString()
    {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        Date today = Calendar.getInstance().getTime();
        return df.format(today);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class MyTextNoteFragment extends Fragment implements MyNoteFragmentProtocols
    {
        public static String CLASS_ID_KEY = "class_id_key";
        private byte[] data;
        private View rootView;

        public MyTextNoteFragment()
        {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {
            rootView = inflater.inflate(R.layout.fragment_my_text_note, container, false);
            if (data != null)
            {
                String s = new String(data);
                ((EditText) rootView.findViewById(R.id.textNoteEditText)).setText(s);
            }
            return rootView;
        }

        @Override
        public byte[] getNoteData()
        {
            return ((EditText) rootView.findViewById(R.id.textNoteEditText)).getText().toString().getBytes();
        }

        @Override
        public void setNoteData(byte[] data)
        {
            this.data = data;
            if (rootView != null)
            {
                ((EditText) rootView.findViewById(R.id.textNoteEditText)).setText(new String(data));
            }
        }
    }
}
