package helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Oleksiy on 5/13/2014.
 */
public class MyFileWriter
{
    public static File getInternalStoragePath(Context context)
    {
        return context.getFilesDir();
    }

    public static File getExternalStoragePath()
    {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    }

    public static File appendFileNameToPath(File path, String fileName)
    {
        return new File(path, fileName);
    }

    public static Uri saveBitmapToFile(File path, Bitmap bmp)
    {
        try
        {
            FileOutputStream fileOutputStream = new FileOutputStream(path);
            boolean status = bmp.compress(Bitmap.CompressFormat.JPEG, 33, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            return Uri.parse("file://" + path.getAbsolutePath());
        } catch (Exception ex)
        {
            return null;
        }
    }

    public static Uri getUriForImageFileFromBytesPrivate(Context c, byte[] data)
    {
        return getUriForImageFileFromBytesPrivate(c, data, "temp.jpeg");
    }

    public static Uri getUriForImageFileFromBytesPrivate(Context c, byte[] data, String name)
    {
        Bitmap b = BitmapFactory.decodeByteArray(data, 0, data.length);
        Uri uri = MyFileWriter.saveBitmapToFile(MyFileWriter.appendFileNameToPath(MyFileWriter.getInternalStoragePath(c), name), b);
        Log.i("FileWriter", "size:" + data.length + " private uri request for:" + uri.toString());
        return uri;
    }

    public static Uri getUriForImageFileFromBytes(byte[] data, String name)
    {
        Bitmap b = BitmapFactory.decodeByteArray(data, 0, data.length);
        Uri uri = MyFileWriter.saveBitmapToFile(MyFileWriter.appendFileNameToPath(MyFileWriter.getExternalStoragePath(), name), b);
        Log.i("FileWriter", "size:" + data.length + " uri request for:" + uri.toString());
        return uri;
    }

    public static Uri getUriForImageFile(Bitmap b, String name)
    {
        Uri uri = MyFileWriter.saveBitmapToFile(MyFileWriter.appendFileNameToPath(MyFileWriter.getExternalStoragePath(), name), b);
        return uri;
    }

    public static Uri getUriForImageFileFromBytes(byte[] data)
    {
        Bitmap b = BitmapFactory.decodeByteArray(data, 0, data.length);
        return MyFileWriter.saveBitmapToFile(MyFileWriter.appendFileNameToPath(MyFileWriter.getExternalStoragePath(), "temp_image.jpeg"), b);
    }

    public static String saveBitmapToMedia(Bitmap bmp, Context c, String name)
    {
        return MediaStore.Images.Media.insertImage(c.getContentResolver(), bmp, name, null);
    }

    public static Bitmap bitmapFromByteArra(byte[] data)
    {
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }
}
