package helpers;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by Oleksiy on 10/18/2014.
 */
public class MyFileReader
{
    public static byte[] readFile(String path) throws IOException
    {
        return readFile(new File(path));
    }

    public static byte[] readFile(File file) throws IOException
    {
        RandomAccessFile f = new RandomAccessFile(file, "r");
        try
        {
            long longlength = f.length();
            int length = (int) longlength;
            if (length != longlength)
            {
                throw new IOException("File size >= 2 GB");
            }
            byte[] data = new byte[length];
            f.readFully(data);
            return data;
        } finally
        {
            f.close();
        }
    }
}
