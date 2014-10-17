package school.com.classnotebook.controllers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import helpers.MyFileWriter;
import helpers.MyScreenCapper;
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
                EditText titleText = (EditText) findViewById(R.id.noteTitleEditText);
                titleText.setHint("Title");
                Bundle bundle = new Bundle();
                bundle.putInt(MyTextNoteFragment.CLASS_ID_KEY, classId);
                if (noteType.equals(MyNoteData.Type.text.toString()))
                {
                    setTitle("Text Note");
                    MyTextNoteFragment f = new MyTextNoteFragment();
                    setFragment(f);
                } else if (noteType.equals(MyNoteData.Type.audio.toString()))
                {

                } else if (noteType.equals(MyNoteData.Type.image.toString()))
                {
                    setTitle("Picture note");
                    MyPictureNoteFragment f = new MyPictureNoteFragment();
                    setFragment(f);
                } else if (noteType.equals(MyNoteData.Type.drawing.toString()))
                {
                    setTitle("Drawing Note");
                    MyDrawingNoteFragment f = new MyDrawingNoteFragment();
                    setFragment(f);
                }
            }
        }
    }

    private void setFragment(Fragment f)
    {
        if (noteId != -1)
        {
            MyNoteData n = MyAppDatabase.getInstance(this).getNoteDataSmart(noteId);
            if (f instanceof MyNoteFragmentProtocols)
            {
                MyNoteFragmentProtocols proFrag = ((MyNoteFragmentProtocols) f);
                proFrag.setNoteData(n.getData());
            }

            ((EditText) findViewById(R.id.noteTitleEditText)).setText(n.getName());
        }
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, f)
                .commit();
        if (f instanceof MyNoteFragmentProtocols)
        {
            MyNoteFragmentProtocols proFrag = ((MyNoteFragmentProtocols) f);
            mainFragment = proFrag;
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
                Log.i("!!!!", "here");
                if (mainFragment != null)
                {
                    Log.i("!!!!", "here1");
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
                        MyAppDatabase.getInstance(this).saveNoteDataSmart(new MyNoteData(noteType, dateAsString(), noteName, classId, mainFragment.getNoteData()));
                        Log.i("!!!!", "here2");
                    } else //editing existing
                    {
                        MyAppDatabase.getInstance(this).updateNoteDataSmart(new MyNoteData(noteId, noteType, dateAsString(), noteName, classId, mainFragment.getNoteData()));
                        Log.i("!!!!", "here3");
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

    public static class MyPictureNoteFragment extends Fragment implements MyNoteFragmentProtocols
    {
        private static final int CAMERA_REQUEST = 3333;
        public static String CLASS_ID_KEY = "class_id_key";
        private byte[] data;
        private View rootView;
        private String mCurrentPhotoPath;

        private File createImageFile() throws IOException
        {
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);
            File image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );

            mCurrentPhotoPath = image.getAbsolutePath();
            return image;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {
            rootView = inflater.inflate(R.layout.fragment_my_picture_note, container, false);
            ImageView imgView = (ImageView) rootView.findViewById(R.id.imageNoteImageView);
            if (data != null)
            {
                Bitmap b = BitmapFactory.decodeByteArray(data, 0, data.length);

                imgView.setImageBitmap(b);
            }
            imgView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null)
                    {
                        File photoFile = null;
                        try
                        {
                            photoFile = createImageFile();
                        } catch (IOException ex)
                        {
                            Log.i("MyPictureNoteFragment", ex.getMessage());
                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null)
                        {
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                    Uri.fromFile(photoFile));
                            startActivityForResult(takePictureIntent, CAMERA_REQUEST);
                        } else
                        {
                            //todo show alert warning
                        }
                    }
                }
            });
            return rootView;
        }

        private void setPic()
        {
            // Get the dimensions of the View
            ImageView mImageView = (ImageView) rootView.findViewById(R.id.imageNoteImageView);
            int targetW = 800;//mImageView.getWidth();
            int targetH = 800;//mImageView.getHeight();
            Log.i("MyPictureNoteFragment", "tw" + targetW + "th:" + targetH);
            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;
            Log.i("MyPictureNoteFragment", "pw" + photoW + "ph" + photoH);
            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            mImageView.setOnFocusChangeListener(new View.OnFocusChangeListener()
            {
                @Override
                public void onFocusChange(View view, boolean b)
                {
                    data = getNoteData();
                }
            });
            mImageView.setImageBitmap(bitmap);
        }

        @Override
        public void onStart()
        {
            super.onStart();

        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data)
        {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK)
            {
                setPic();
            }
        }

        @Override
        public byte[] getNoteData()
        {
            ImageView imgView = (ImageView) rootView.findViewById(R.id.imageNoteImageView);
            Bitmap b = ((BitmapDrawable) imgView.getDrawable()).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            if (b != null)
            {

                b.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            } else
            {
                Log.w("MyPictureNoteFragment", "null drawable");
            }
            byte[] bitmapdata = stream.toByteArray();
            return bitmapdata;
        }

        @Override
        public void setNoteData(byte[] data)
        {
            this.data = data;
            if (rootView != null)
            {
                Bitmap b = BitmapFactory.decodeByteArray(data, 0, data.length);
                ImageView imgView = (ImageView) rootView.findViewById(R.id.imageNoteImageView);
                imgView.setImageBitmap(b);
            }
        }
    }

    public static class MyDrawingNoteFragment extends Fragment implements MyNoteFragmentProtocols
    {
        public static String CLASS_ID_KEY = "class_id_key";
        private byte[] data;
        private View rootView;
        private MyWebViewClient mWVC;

        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState)
        {
            View thisView = inflater.inflate(R.layout.fragment_web, container, false);
            WebView webView = (WebView) thisView.findViewById(R.id.drawWebView);
            webView.clearCache(true);
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setAllowFileAccess(true);
            webSettings.setAllowContentAccess(true);

            mWVC = new MyWebViewClient();
            rootView = thisView;
            mWVC.width = rootView.getWidth();
            mWVC.height = rootView.getHeight();
            if (data != null)
            {
                mWVC.backGroudFilePath = MyFileWriter.getUriForImageFileFromBytes(data, "temp_drawing.jpeg");
            }
            webView.setWebViewClient(mWVC);
            webView.loadUrl("file:///android_asset/htmlPaint.html");

            return thisView;
        }

        @Override
        public byte[] getNoteData()
        {
            Bitmap b = MyScreenCapper.getBitmapFromView(rootView);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            if (b != null)
            {

                b.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            } else
            {
                Log.w("MyDrawingNoteFragment", "null drawable");
            }
            data = stream.toByteArray();
            return data;
        }

        @Override
        public void setNoteData(byte[] data)
        {
            this.data = data;
            if (mWVC != null)
            {
                mWVC.backGroudFilePath = MyFileWriter.getUriForImageFileFromBytes(data, "temp_drawing.jpeg");
            }
        }

        private class MyWebViewClient extends WebViewClient
        {
            int width;
            int height;
            Uri backGroudFilePath;

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url)
            {
                // view.loadUrl("javascript:drawPaintCanvasWithSize("+width+","+height+")");
                if (backGroudFilePath != null)
                {
                    Log.i("MyDrawingNoteFragment", "bg:" + backGroudFilePath.toString());
                    //view.loadUrl("javascript:setBackground('"+ backGroudFilePath.toString() + "')");
                    view.loadUrl("javascript:drawPaintCanvasWithBackground('" + backGroudFilePath.toString() + "')");
                } else
                {
                    view.loadUrl("javascript:drawPaintCanvas()");
                }
            }

        }
    }
}
