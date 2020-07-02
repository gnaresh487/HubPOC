package com.naresh.hubpoc.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.naresh.hubpoc.R;
import com.naresh.hubpoc.SharedPrefUtils;
import com.naresh.hubpoc.activity.MainActivity;
import com.naresh.hubpoc.activity.SettingsActivity;

import java.io.File;

public class ForegroundService extends Service {
    public static final String TAG = ForegroundService.class.getSimpleName();
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    private MediaRecorder recorder;
    private AudioManager am;
    public static boolean isRecordStarted, isStartRecord;
    int count;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        am=(AudioManager)getSystemService(Context.AUDIO_SERVICE);

        String input = intent.getStringExtra("inputExtra");

        createNotificationChannel();

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Foreground Service")
                .setContentText(input)
                .setSmallIcon(R.drawable.ic_record_voice_over_black_24dp)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);

        Log.d(TAG, "onStartCommand: count "+ (++count));
        startMediaRecorder(SharedPrefUtils.getIntData(this, SettingsActivity.AUDIO_SOURCE));
        //startMediaRecorder(MediaRecorder.AudioSource.VOICE_COMMUNICATION);

        /*if(intent.getAction() != null && intent.getAction().equals("STOP_ACTION")) {
            stopRecorder();
            stopForeground(true);
            stopSelf();
        }*/
        //do heavy work on a background thread
        //stopSelf();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        stopRecorder();
        super.onDestroy();
        Intent serviceIntent = new Intent(this, MainActivity.class);
        //serviceIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        serviceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(serviceIntent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {

            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = getString(R.string.app_name);
                String description = getString(R.string.app_name);
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                channel.setDescription(description);
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
    }

    private boolean startMediaRecorder(final int audioSource){
        Toast.makeText(this, "Record stared foreground service ", Toast.LENGTH_SHORT).show();
        recorder = new MediaRecorder();
        if(am != null) {
            am.setSpeakerphoneOn(true);
            Toast.makeText(this, "speaker on", Toast.LENGTH_SHORT).show();
           // am.setStreamVolume(AudioManager.STREAM_VOICE_CALL, am.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL), 0);
//            Toast.makeText(this, am.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL), Toast.LENGTH_SHORT).show();
           // am.setMode(AudioManager.MODE_IN_COMMUNICATION);
  //          Toast.makeText(this, am.getStreamVolume(AudioManager.STREAM_VOICE_CALL), Toast.LENGTH_SHORT).show();
        }
        try{
            File dirPath = new File(getExternalFilesDir(null).getAbsolutePath() + "/tempFiles");

            Log.d(TAG, "startMediaRecorder: dirPath "+dirPath);
            if (!dirPath.exists()) {
                dirPath.mkdirs();
            }
            String fileName = dirPath.getAbsolutePath() + File.separator + System.currentTimeMillis()+".amr";

            recorder.reset();
            recorder.setAudioSource(audioSource);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setOutputFile(fileName);

            MediaRecorder.OnErrorListener errorListener = (arg0, arg1, arg2) -> {
                Log.e("TAG", "OnErrorListener " + arg1 + "," + arg2);
                // terminateAndEraseFile();
            };
            recorder.setOnErrorListener(errorListener);

            MediaRecorder.OnInfoListener infoListener = (arg0, arg1, arg2) -> {
                Log.e(TAG, "OnInfoListener " + arg1 + "," + arg2);
                //terminateAndEraseFile();
            };
            recorder.setOnInfoListener(infoListener);

            recorder.prepare();
            // Sometimes prepare takes some time to complete
            //Thread.sleep(2000);
            recorder.start();
            //isRecordStarted = true;
            return true;
        }catch (Exception e){
            Log.d(TAG, "startMediaRecorder: "+ e.getMessage());
            return false;
        }
    }

    private void stopRecorder(){
        Toast.makeText(this, "Record saved foreground service ", Toast.LENGTH_SHORT).show();
        if(am != null) {
            am.setMode(AudioManager.MODE_NORMAL);
        }
       // if (!isRecordStarted) {
            //recorder.stop();
            recorder.release();
            recorder = null;
            //isRecordStarted = false;
       // }
        stopForeground(true);
        stopSelf();
    }

    public static int getAudioSource(String str) {
        if (str.equals("MIC")) {
            return MediaRecorder.AudioSource.MIC;
        }
        else if (str.equals("VOICE_COMMUNICATION")) {
            return MediaRecorder.AudioSource.VOICE_COMMUNICATION;
        }
        else if (str.equals("VOICE_CALL")) {
            return MediaRecorder.AudioSource.VOICE_CALL;
        }
        else if (str.equals("VOICE_DOWNLINK")) {
            return MediaRecorder.AudioSource.VOICE_DOWNLINK;
        }
        else if (str.equals("VOICE_UPLINK")) {
            return MediaRecorder.AudioSource.VOICE_UPLINK;
        }
        else if (str.equals("VOICE_RECOGNITION")) {
            return MediaRecorder.AudioSource.VOICE_RECOGNITION;
        }
        else if (str.equals("CAMCORDER")) {
            return MediaRecorder.AudioSource.CAMCORDER;
        }
        else {
            return MediaRecorder.AudioSource.DEFAULT;
        }
    }

}