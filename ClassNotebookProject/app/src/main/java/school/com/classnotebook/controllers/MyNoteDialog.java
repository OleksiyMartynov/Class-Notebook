package school.com.classnotebook.controllers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import models.containers.MyNoteData;
import models.database.MyAppDatabase;

/**
 * Created by Oleksiy on 10/16/2014.
 */
public class MyNoteDialog extends DialogFragment
{
    private int classId = -1;
    private MyDialogListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Pick note type")
                .setItems(new String[]{MyNoteData.Type.text.toString(), MyNoteData.Type.audio.toString(), MyNoteData.Type.image.toString(), MyNoteData.Type.drawing.toString()}, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        switch (which)
                        {
                            case 0:
                            {

                                if (mListener != null)
                                {
                                    mListener.onPositiveButtonClicked();
                                }
                                Intent intent = new Intent(getActivity(), MyNoteActivity.class);
                                intent.putExtra(MyNoteActivity.NOTE_TYPE, MyNoteData.Type.text.toString());
                                intent.putExtra(MyNoteActivity.CLASS_ID, getClassId());
                                getActivity().startActivity(intent);
                                break;
                            }
                            case 1:
                            {
                                MyAppDatabase.getInstance(getActivity()).saveNoteData(new MyNoteData(MyNoteData.Type.audio.toString(), dateAsString(), "test audio note", classId));
                                if (mListener != null)
                                {
                                    mListener.onPositiveButtonClicked();
                                }
                                break;
                            }
                            case 2:
                            {
                                if (mListener != null)
                                {
                                    mListener.onPositiveButtonClicked();
                                }
                                Intent intent = new Intent(getActivity(), MyNoteActivity.class);
                                intent.putExtra(MyNoteActivity.NOTE_TYPE, MyNoteData.Type.image.toString());
                                intent.putExtra(MyNoteActivity.CLASS_ID, getClassId());
                                getActivity().startActivity(intent);
                                break;
                            }
                            case 3:
                            {
                                MyAppDatabase.getInstance(getActivity()).saveNoteData(new MyNoteData(MyNoteData.Type.drawing.toString(), dateAsString(), "test drawing note", classId));
                                if (mListener != null)
                                {
                                    mListener.onPositiveButtonClicked();
                                }
                                break;
                            }
                        }
                    }
                });
        return builder.create();
    }

    public void setListener(MyDialogListener listener)
    {
        mListener = listener;
    }

    public int getClassId()
    {
        return classId;
    }

    public void setClassId(int id)
    {
        classId = id;
    }

    private String dateAsString()
    {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        Date today = Calendar.getInstance().getTime();
        return df.format(today);
    }
}
