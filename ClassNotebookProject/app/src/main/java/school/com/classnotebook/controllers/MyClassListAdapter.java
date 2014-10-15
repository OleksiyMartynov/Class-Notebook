package school.com.classnotebook.controllers;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import models.containers.MyClassData;
import school.com.classnotebook.R;

/**
 * Created by Oleksiy on 10/15/2014.
 */
public class MyClassListAdapter extends ArrayAdapter<MyClassData>
{
    public Context myContext;
    private List<MyClassData> myObjects;

    public MyClassListAdapter(Context context, int resource, List<MyClassData> objects)
    {
        super(context, resource, objects);
        this.myContext = context;
        this.myObjects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = null;
        MyClassData classData = getItem(position);
        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.class_list_cell, null);
            holder = new ViewHolder();
            holder.className = (TextView) convertView.findViewById(R.id.classNameTextVIew);
            holder.classProff = (TextView) convertView.findViewById(R.id.proffNameTextView);
            holder.classDate = (TextView) convertView.findViewById(R.id.classDateTextView);
            convertView.setTag(holder);
        } else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.className.setText(classData.getName());
        holder.classProff.setText(classData.getProff());
        holder.classDate.setText(classData.getDate());

        return convertView;
    }

    @Override
    public void remove(MyClassData o)
    {
        super.remove(o);
    }

    public class ViewHolder
    {
        TextView className;
        TextView classProff;
        TextView classDate;
    }
}