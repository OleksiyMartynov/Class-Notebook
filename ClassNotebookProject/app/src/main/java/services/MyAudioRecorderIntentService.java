package services;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.SystemClock;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import java.io.IOException;
import java.util.ArrayList;

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
    private static final int ALARM_ID = 123;
    private static final String ACTION_TICK = "tick_event";
    private static MyRecorderState state = MyRecorderState.stoped;
    private static MyAudioRecorderIntentServiceFeedbackListener listener;
    private static MediaRecorder recorder;
    private static String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/audiorecord.3gp";
    private static SpeechRecognizer sr;
    private static NotificationCompat.Builder notiBuilder;
    private static Bundle activityCallbackBundle;
    private static int seconds = 0;
    private static PendingIntent alarmIntent;
    public static void setListener(MyAudioRecorderIntentServiceFeedbackListener listener)
    {
        MyAudioRecorderIntentService.listener = listener;
    }

    public static void startRecording(Context context, MyAudioRecorderIntentServiceFeedbackListener listener, Bundle b)
    {
        activityCallbackBundle = b;
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
                Log.i("MyAudioRecordService", "new action:" + action);
                handleActionStart();
            } else if (ACTION_STOP_RECORDING.equals(action))
            {
                Log.i("MyAudioRecordService", "new action:" + action);
                handleActionStop();
            } else if (ACTION_CANCEL_RECORDING.equals(action))
            {
                Log.i("MyAudioRecordService", "new action:" + action);
                handleActionCancel();
            } else if (ACTION_TICK.equals(action))
            {
                onTick();
            }
        }
        return Service.START_STICKY;
    }

    private void onTick()
    {
        ++seconds;
        updateNotification(getSmallNotiObj(seconds));
        if (listener != null)
        {
            listener.onTick(seconds);
        }
    }

    private void startTimer()
    {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MyAudioRecorderIntentService.class);
        intent.setAction(MyAudioRecorderIntentService.ACTION_TICK);

        alarmIntent = PendingIntent.getService(this, ALARM_ID, intent, 0);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000, 1000, alarmIntent);
    }

    private void stopTimer()
    {
        if (alarmIntent != null)
        {

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(alarmIntent);
        }
    }
    private Notification getSmallNotiObj(int s)
    {
        Intent tapIntent = new Intent(this, MyNoteActivity.class);
        if(activityCallbackBundle!=null) {
            tapIntent.putExtras(activityCallbackBundle);
        }
        PendingIntent pIntent = PendingIntent.getActivity(this,0,tapIntent,0);
        if(notiBuilder==null)
        {
            notiBuilder=new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_stat_ic_notification_notebook)
                    .setContentTitle("Recording...")
                    .setOngoing(true);
        }
        String sec = ""+(s%60);
        String min = s/60==0?"00":""+s/60;
        String hr = s/60/60==0?"00":""+s/60/60;
        notiBuilder.setContentText(hr+":"+min+":"+sec);
        notiBuilder.setContentIntent(pIntent);
        return notiBuilder.build();
    }
    private Notification getNotificationObjDEPR(int s)
    {
        Intent tapIntent = new Intent(this, MyNoteActivity.class);
        //tapIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        if (activityCallbackBundle != null)
        {
            tapIntent.putExtras(activityCallbackBundle);
        }

        Intent stopIntent = new Intent(this, MyAudioRecorderIntentService.class);
        stopIntent.setAction(ACTION_STOP_RECORDING);

        PendingIntent pendingTapIntent = PendingIntent.getActivity(this, 0, tapIntent, 0);
        PendingIntent pendingStopIntent = PendingIntent.getService(this, 0, stopIntent, 0);

        RemoteViews notiView = new RemoteViews(getPackageName(), R.layout.custom_notification);
        notiView.setOnClickPendingIntent(R.id.notiSaveImageButton, pendingStopIntent);
        notiView.setOnClickPendingIntent(R.id.notiOpenActivityButton, pendingTapIntent);
        notiView.setTextViewText(R.id.notiSecondsTextView, "" + (s % 60));
        notiView.setTextViewText(R.id.notiMinutesTextView, "" + ((s / 60) == 0 ? "00" : (s / 60)));
        notiView.setTextViewText(R.id.notiHoursTextView, "" + ((s / 60 / 60) == 0 ? "00" : (s / 60 / 60)));

        if (notiBuilder == null)
        {
            notiBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_stat_ic_notification_notebook)
                            .setOngoing(true);
        } else
        {
            notiBuilder.setContent(notiView);
        }
        //notiBuilder.setContentIntent(pendingTapIntent);
        return notiBuilder.build();
    }

    private void showNotification(Notification notification)
    {
        startForeground(ONGOING_NOTIFICATION_ID, notification);
    }

    private void updateNotification(Notification notification)
    {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(ONGOING_NOTIFICATION_ID, notification);
    }
    private void hideNotification()
    {
        stopForeground(true);
    }

    private void startVoiceRecognition()
    {
        boolean available = SpeechRecognizer.isRecognitionAvailable(this);
        if (available)
        {
            sr = SpeechRecognizer.createSpeechRecognizer(this);
            sr.setRecognitionListener(new RecognitionListener()
            {
                @Override
                public void onReadyForSpeech(Bundle bundle)
                {

                }

                @Override
                public void onBeginningOfSpeech()
                {

                }

                @Override
                public void onRmsChanged(float v)
                {

                }

                @Override
                public void onBufferReceived(byte[] bytes)
                {

                }

                @Override
                public void onEndOfSpeech()
                {

                }

                @Override
                public void onError(int i)
                {

                }

                @Override
                public void onResults(Bundle results)
                {
                    ArrayList<String> resultList = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    Log.i("MyAudioRecordingService", "Voice Recognition results:");
                    for (String str : resultList)
                    {
                        Log.i("MyAudioRecordingService", str);
                    }
                    //todo store results
                }

                @Override
                public void onPartialResults(Bundle bundle)
                {

                }

                @Override
                public void onEvent(int i, Bundle bundle)
                {

                }
            });
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getApplicationContext().getPackageName());
            sr.startListening(intent);
        } else
        {
            Log.i("MyAudioRecordingService", "voice recognition not available");
        }
    }

    private void stopVoiceRecognition()
    {
        sr.stopListening();
    }
    private void handleActionStart()
    {
        if (state == MyRecorderState.stoped)
        {
            startVoiceRecognition();
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
            showNotification(getSmallNotiObj(seconds));
            startTimer();
        }
    }

    private void handleActionStop()
    {
        if (state == MyRecorderState.recording)
        {
            stopStuff();
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
        }
    }

    private void handleActionCancel()
    {
        stopStuff();
    }

    private void stopStuff()
    {
        if (state == MyRecorderState.recording)
        {
            stopVoiceRecognition();
            recorder.stop();
            recorder.reset();
            recorder.release();
            recorder = null;
            state = MyRecorderState.stoped;
            if (listener != null)
            {
                listener.onStateChange(state);
            }
            stopTimer();
            hideNotification();
            seconds = 0;
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
