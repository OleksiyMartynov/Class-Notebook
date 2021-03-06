package school.com.classnotebook.controllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import models.containers.MyClassData;
import models.database.MyAppDatabase;
import school.com.classnotebook.R;

/**
 * Created by Oleksiy on 10/15/2014.
 */
public class MyClassDialog extends DialogFragment
{
    private MyDialogListener mListener;
    private MyClassData classData;

    public void setClassData(MyClassData data)
    {
        this.classData = data;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View v = inflater.inflate(R.layout.dialog_class_info_layout, null);
        TextView title = (TextView) v.findViewById(R.id.classDialogTitleTextVIew);

        final EditText className = (EditText) v.findViewById(R.id.classDialogClassNameEditText);
        final EditText classProffName = (EditText) v.findViewById(R.id.classDialogTeacherNameEditText);
        final DatePicker date = (DatePicker) v.findViewById(R.id.classDialogDatePicker);

        if (classData != null)
        {
            title.setText("Edit Class");
            className.setText(classData.getName());
            classProffName.setText(classData.getProff());
        }

        builder.setView(v).setPositiveButton("Save", null)

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        if (mListener != null)
                        {
                            mListener.onNegativeButtonClicked();
                        }
                        MyClassDialog.this.getDialog().cancel();
                    }
                });

        final AlertDialog d = builder.create();
        d.setOnShowListener(new DialogInterface.OnShowListener()
        {
            @Override
            public void onShow(DialogInterface dialogInterface)
            {
                Button save = d.getButton(AlertDialog.BUTTON_POSITIVE);
                save.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {

                        //TimePicker time = (TimePicker) v.findViewById(R.id.classDialogTimePicker);
                        if (!valid(className.getText().toString(), classProffName.getText().toString()))
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage("Please enter a valid class name.")
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
                        } else
                        {
                            MyAppDatabase classDb = MyAppDatabase.getInstance(getActivity());
                            if (classData != null)
                            {
                                classData.setName(className.getText().toString());
                                classData.setProff(classProffName.getText().toString());
                                classData.setDate(date.getMonth() + "/" + date.getDayOfMonth() + "/" + date.getYear());
                                classDb.updateClassData(classData);
                            } else
                            {
                                classDb.saveClassData(new MyClassData(className.getText().toString(), classProffName.getText().toString(), date.getMonth() + "/" + date.getDayOfMonth() + "/" + date.getYear()));//+ " " + time.getCurrentHour() + ":" + time.getCurrentMinute()
                            }
                            if (mListener != null)
                            {
                                mListener.onPositiveButtonClicked();
                            }
                            d.dismiss();
                        }
                    }
                });
            }
        });
        return d;
    }

    private boolean valid(String className, String classProffName)
    {
        if (className == null || className.isEmpty())
        {
            return false;
        }
        return true;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try
        {
            mListener = (MyDialogListener) activity;
        } catch (ClassCastException e)
        {
            //throw new ClassCastException(activity.toString() + " must implement NoticeDialogListener");
        }
    }
}
