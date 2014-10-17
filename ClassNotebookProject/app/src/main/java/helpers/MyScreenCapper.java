package helpers;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.ScrollView;

/**
 * Created by Oleksiy on 5/13/2014.
 */
public class MyScreenCapper
{
    static public Bitmap getBitmapFromView(View view)
    {
        Bitmap bitmap;
        View v1 = view;//view.getRootView();
        v1.setDrawingCacheEnabled(true);
        bitmap = Bitmap.createBitmap(v1.getDrawingCache());
        v1.setDrawingCacheEnabled(false);
        return bitmap;
    }

    static public Bitmap getBitmapForScrollView(HorizontalScrollView sv)
    {
        View u = sv;
        u.setDrawingCacheEnabled(true);
        HorizontalScrollView z = sv;
        int totalHeight = z.getChildAt(0).getHeight();
        int totalWidth = z.getChildAt(0).getWidth();
        u.layout(0, 0, totalWidth, totalHeight);
        u.buildDrawingCache(true);
        Bitmap b = Bitmap.createBitmap(u.getDrawingCache());
        u.setDrawingCacheEnabled(false);
        return b;

    }

    static public Bitmap getBitmapForScrollView(ScrollView sv)
    {
        View u = sv;
        u.setDrawingCacheEnabled(true);
        ScrollView z = sv;
        int totalHeight = z.getChildAt(0).getHeight();
        int totalWidth = z.getChildAt(0).getWidth();
        u.layout(0, 0, totalWidth, totalHeight);
        u.buildDrawingCache(true);
        Bitmap b = Bitmap.createBitmap(u.getDrawingCache());
        u.setDrawingCacheEnabled(false);
        return b;

    }

    static public Bitmap getBitmapForScrollView(ListView sv)
    {
        View u = sv;
        u.setDrawingCacheEnabled(true);
        ListView z = sv;
        int totalHeight = z.getChildAt(0).getHeight();
        int totalWidth = z.getChildAt(0).getWidth();
        u.layout(0, 0, totalWidth, totalHeight);
        u.buildDrawingCache(true);
        Bitmap b = Bitmap.createBitmap(u.getDrawingCache());
        u.setDrawingCacheEnabled(false);
        return b;

    }
}
