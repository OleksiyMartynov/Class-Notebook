package school.com.classnotebook.controllers;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import models.containers.MyNoteData;
import school.com.classnotebook.R;

/**
 * Created by Oleksiy on 10/16/2014.
 */
public class MyNoteListAdapter extends ArrayAdapter<MyNoteData>
{
    public Context myContext;
    private List<MyNoteData> myObjects;

    public MyNoteListAdapter(Context context, int resource, List<MyNoteData> objects)
    {
        super(context, resource, objects);
        this.myContext = context;
        this.myObjects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = null;
        MyNoteData noteData = getItem(position);
        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.class_list_cell, null);
            holder = new ViewHolder();
            holder.noteName = (TextView) convertView.findViewById(R.id.noteNameTextView);
            holder.noteDate = (TextView) convertView.findViewById(R.id.noteDateTextView);
            holder.noteTypeImage = (ImageView) convertView.findViewById(R.id.noteTypeImageView);
            convertView.setTag(holder);
        } else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.noteName.setText(noteData.getName());
        holder.noteDate.setText(noteData.getDate());
        //todo switch on note type and set image of noteTypeImage view;

        return convertView;
    }

    @Override
    public void remove(MyNoteData o)
    {
        super.remove(o);
    }

    public class ViewHolder
    {
        TextView noteName;
        TextView noteDate;
        ImageView noteTypeImage;
    }
}
