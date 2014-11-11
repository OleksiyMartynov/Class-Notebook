package school.com.classnotebook.controllers;

/**
 * Created by Oleksiy on 10/16/2014.
 */
public interface MyNoteFragmentProtocols
{
    //public byte[] getNoteData();
    public void getNoteData(MyNoteFragmentDataCallBack callback);

    public void setNoteData(byte[] data);

    public void requestStop();
    public interface MyNoteFragmentDataCallBack
    {
        void onDataReady(byte[] data);
    }
}
