package services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import java.io.IOException;

import helpers.MyFileReader;
import school.com.classnotebook.R;
import school.com.classnotebook.controllers.MyNoteActivity;

/**
 * Created by Oleksiy on 10/18/2014.
 */
public class MyAudioRecorderIntentService extends Service
{
    public static final String ACTION_START_RECORDING = "start_recording";
    ;
    public static final String ACTION_STOP_RECORDING = "stop_recording";
    public static final String ACTION_CANCEL_RECORDING = "cancel_recording";
    private static final int ONGOING_NOTIFICATION_ID = 333;
    private static MyRecorderState state = MyRecorderState.stoped;
    private static MyAudioRecorderIntentServiceFeedbackListener listener;
    private static MediaRecorder recorder;
    private static String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/audiorecord.3gp";

    public static void setListener(MyAudioRecorderIntentServiceFeedbackListener listener)
    {
        MyAudioRecorderIntentService.listener = listener;
    }

    public static void startRecording(Context context, MyAudioRecorderIntentServiceFeedbackListener listener)
    {
        MyAudioRecorderIntentService.listener = listener;
        Intent intent = new Intent(context, MyAudioRecorderIntentService.class);
        intent.setAction(ACTION_START_RECORDING);
        context.startService(intent);
    }

    public static void stopRecording(Context context)
    {
        Intent intent = new Intent(context, MyAudioRecorderIntentService.class);
        intent.setAction(ACTION_STOP_RECORDING);
        context.startService(intent);
    }

    public static void cancelRecording(Context context)
    {
        Intent intent = new Intent(context, MyAudioRecorderIntentService.class);
        intent.setAction(ACTION_CANCEL_RECORDING);
        context.startService(intent);
    }

    public static MyRecorderState getState()
    {
        return state;
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        if (intent != null)
        {
            final String action = intent.getAction();
            if (ACTION_START_RECORDING.equals(action))
            {
                handleActionStart();
            } else if (ACTION_STOP_RECORDING.equals(action))
            {
                handleActionStop();
            } else if (ACTION_CANCEL_RECORDING.equals(action))
            {
                handleActionCancel();
            }
        }
        return Service.START_STICKY;
    }

    private void showNotification()
    {
        //todo finish notification
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");
        Intent resultIntent = new Intent(this, MyNoteActivity.class);//todo pass note id

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, resultIntent, 0);
        mBuilder.setContentIntent(pendingIntent);
        startForeground(ONGOING_NOTIFICATION_ID, mBuilder.build());
    }

    private void hideNotification()
    {
        stopForeground(true);
    }

    private void handleActionStart()
    {
        if (state == MyRecorderState.stoped)
        {

            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setOutputFile(MyAudioRecorderIntentService.filePath);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            try
            {
                recorder.prepare();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            recorder.start();
            //todo start timer and call listener.onTick;
            state = MyRecorderState.recording;
            if (listener != null)
            {
                listener.onStateChange(state);
            }
            showNotification();
        }
    }

    private void handleActionStop()
    {
        if (state == MyRecorderState.recording)
        {
            recorder.stop();
            recorder.release();
            recorder = null;
            state = MyRecorderState.stoped;
            if (listener != null)
            {
                listener.onStateChange(state);
            }

            if (listener != null)
            {
                try
                {
                    listener.onFinishedRecording(MyFileReader.readFile(filePath));
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            hideNotification();
        }
    }

    private void handleActionCancel()
    {
        if (state == MyRecorderState.recording)
        {
            recorder.stop();
            recorder.reset();
            recorder.release();
            recorder = null;
            state = MyRecorderState.stoped;
            if (listener != null)
            {
                listener.onStateChange(state);
            }
            hideNotification();
        }
    }

    public enum MyRecorderState
    {
        recording, stoped
    }

    public interface MyAudioRecorderIntentServiceFeedbackListener
    {
        public void onTick(int timeSeconds);

        public void onFinishedRecording(byte[] data);

        public void onStateChange(MyRecorderState state);
    }
}
