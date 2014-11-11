package school.com.classnotebook.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import helpers.MyFileWriter;
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
        final MyNoteData noteData = getItem(position);
        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.note_list_cell, null);
            holder = new ViewHolder();
            holder.noteName = (TextView) convertView.findViewById(R.id.noteNameTextView);
            holder.noteDate = (TextView) convertView.findViewById(R.id.noteDateTextView);
            holder.noteTypeImage = (ImageView) convertView.findViewById(R.id.noteTypeImageView);
            holder.noteShareButton = (ImageButton) convertView.findViewById(R.id.shareNoteButton);
            convertView.setTag(holder);
        } else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.noteName.setText(noteData.getName());
        holder.noteDate.setText(noteData.getDate());
        holder.noteShareButton.setFocusable(false);
        holder.noteShareButton.setFocusableInTouchMode(false);
        if (noteData.getTypeOfData().equals(MyNoteData.Type.audio.toString()))
        {
            holder.noteTypeImage.setImageResource(R.drawable.ic_action_audio_logo);
            //todo implement sharing for audio
            holder.noteShareButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Log.i("NoteAdapter", " share audio note tapped");
                    try
                    {
                        Intent intent = new Intent(Intent.ACTION_SEND);

                        intent.putExtra(Intent.EXTRA_TEXT, noteData.getName());
                        intent.putExtra(Intent.EXTRA_STREAM, MyFileWriter.getUriFromAudioFileFromBytes(noteData.getData(), "/audiorecord.3gp"));
                        intent.setType("audio/*");
                        getContext().startActivity(Intent.createChooser(intent, "Share audio note"));
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        } else if (noteData.getTypeOfData().equals(MyNoteData.Type.drawing.toString()))
        {
            holder.noteTypeImage.setImageResource(R.drawable.ic_action_draw_logo);
            //todo implement sharing for image drawings
            holder.noteShareButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Log.i("NoteAdapter", " share drawing note tapped");
                    Intent intent = new Intent(Intent.ACTION_SEND);

                    intent.putExtra(Intent.EXTRA_TEXT, noteData.getName());

                    intent.putExtra(Intent.EXTRA_STREAM, MyFileWriter.getUriForImageFileFromBytes(noteData.getData(), "temp_drawing.jpeg"));
                    intent.setType("image/jpeg");
                    getContext().startActivity(Intent.createChooser(intent, "Share drawing note"));
                }
            });
        } else if (noteData.getTypeOfData().equals(MyNoteData.Type.image.toString()))
        {
            holder.noteTypeImage.setImageResource(R.drawable.ic_action_picture_logo);
            holder.noteShareButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Log.i("NoteAdapter", " share image note tapped");
                    Intent intent = new Intent(Intent.ACTION_SEND);

                    intent.putExtra(Intent.EXTRA_TEXT, noteData.getName());

                    intent.putExtra(Intent.EXTRA_STREAM, MyFileWriter.getUriForImageFileFromBytes(noteData.getData(), "temp_image.jpeg"));
                    intent.setType("image/jpeg");
                    getContext().startActivity(Intent.createChooser(intent, "Share image note"));
                }
            });
        } else if (noteData.getTypeOfData().equals(MyNoteData.Type.text.toString()))
        {

            holder.noteTypeImage.setImageResource(R.drawable.ic_action_text_logo);
            holder.noteShareButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Log.i("NoteAdapter", "share text note tapped");
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, noteData.getName());
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, new String(noteData.getData()));
                    getContext().startActivity(Intent.createChooser(sharingIntent, "Share text note"));
                }
            });
        }

        return convertView;
    }

    private Uri getImageUri(byte[] data)
    {
        Bitmap b = BitmapFactory.decodeByteArray(data, 0, data.length);
        return MyFileWriter.saveBitmapToFile(MyFileWriter.appendFileNameToPath(MyFileWriter.getExternalStoragePath(), "temp_image.jpeg"), b);
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
        ImageButton noteShareButton;
    }
}
