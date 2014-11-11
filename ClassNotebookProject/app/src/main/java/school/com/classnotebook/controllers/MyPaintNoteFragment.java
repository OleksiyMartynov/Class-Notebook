package school.com.classnotebook.controllers;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import helpers.MyFileWriter;
import helpers.MyScreenCapper;
import school.com.classnotebook.R;
import school.com.classnotebook.controllers.custom.MyPaintView;

/**
 * Created by Oleksiy on 10/16/2014.
 */
public class MyPaintNoteFragment extends Fragment implements MyNoteFragmentProtocols
{
    View rootView;
    MyPaintView paintView;
    private byte[] data;

    public MyPaintNoteFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_my_paint, container, false);
        paintView = (MyPaintView) rootView.findViewById(R.id.paintView);
        if (data != null)
        {
            paintView.setBackground(MyFileWriter.bitmapFromByteArra(data));
        }
        Button btnRed = (Button) rootView.findViewById(R.id.redBtn);
        Button btnGreen = (Button) rootView.findViewById(R.id.greenBtn);
        Button btnBlue = (Button) rootView.findViewById(R.id.blueBtn);
        Button btnBlack = (Button) rootView.findViewById(R.id.blackBtn);
        Button btnWhite = (Button) rootView.findViewById(R.id.whiteBtn);
        Button btnClear = (Button) rootView.findViewById(R.id.clearBtn);
        View.OnClickListener btnListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int id = view.getId();
                switch (id)
                {
                    case R.id.redBtn:
                    {
                        paintView.setBrushColor(Color.RED);
                        break;
                    }
                    case R.id.greenBtn:
                    {
                        paintView.setBrushColor(Color.GREEN);
                        break;
                    }
                    case R.id.blueBtn:
                    {
                        paintView.setBrushColor(Color.BLUE);
                        break;
                    }
                    case R.id.blackBtn:
                    {
                        paintView.setBrushColor(Color.BLACK);
                        break;
                    }
                    case R.id.whiteBtn:
                    {
                        paintView.setBrushColor(Color.WHITE);
                        break;
                    }
                    case R.id.clearBtn:
                    {
                        paintView.clear();
                        break;
                    }
                }
            }
        };
        btnRed.setOnClickListener(btnListener);
        btnGreen.setOnClickListener(btnListener);
        btnBlue.setOnClickListener(btnListener);
        btnBlack.setOnClickListener(btnListener);
        btnWhite.setOnClickListener(btnListener);
        btnClear.setOnClickListener(btnListener);

        return rootView;
    }


    @Override
    public void getNoteData(MyNoteFragmentDataCallBack callBack)
    {
        Bitmap b = paintView.getViewAsBitmap();
        data = MyScreenCapper.bitmapToByteArr(b);
        callBack.onDataReady(data);
    }

    @Override
    public void setNoteData(byte[] data)
    {
        this.data = data;
        if (rootView != null)
        {
            paintView.setBackground(MyFileWriter.bitmapFromByteArra(data));
        }
    }

    @Override
    public void requestStop()
    {

    }
}
