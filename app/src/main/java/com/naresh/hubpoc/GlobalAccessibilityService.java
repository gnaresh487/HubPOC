package com.naresh.hubpoc;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.naresh.hubpoc.activity.MainActivity;
import com.naresh.hubpoc.service.ForegroundService;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class GlobalAccessibilityService extends AccessibilityService {

    private static final String TAG = GlobalAccessibilityService.class.getSimpleName();
    //static WeakReference<GlobalAccessibilityService> ref = null;

    private AudioManager audioManager;
    private MediaRecorder recorder;
    public static boolean isRecordStarted;

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: ");
        super.onCreate();
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        /*if(ref != null && ref.get() != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                ref.get().disableSelf();
            }
        }*/
    }

    /*
    public void startSpeechRecognition() {
        // Fire an intent to start the speech recognition activity.
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        // secret parameters that when added provide audio url in the result
        intent.putExtra("android.speech.extra.GET_AUDIO_FORMAT", "audio/AMR");
        intent.putExtra("android.speech.extra.GET_AUDIO", true);

        startActivityForResult(intent, "<some code you choose>");
    }

    // handle result of speech recognition
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // the resulting text is in the getExtras:
        Bundle bundle = data.getExtras();
        ArrayList<String> matches = bundle.getStringArrayList(RecognizerIntent.EXTRA_RESULTS)
        // the recording url is in getData:
        Uri audioUri = data.getData();
        ContentResolver contentResolver = getContentResolver();
        InputStream filestream = contentResolver.openInputStream(audioUri);
        // TODO: read audio file from inputstream
    }*/

    private boolean startMediaRecorder(final int audioSource) {
        Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show();
        recorder = new MediaRecorder();
        AudioManager am=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
        assert am != null;
        am.setMode(AudioManager.MODE_IN_COMMUNICATION);
        try {
            File dirPath = new File(getExternalFilesDir(null).getAbsolutePath() + "/tempFiles");

            Log.d(TAG, "startMediaRecorder: dirPath " + dirPath);
            if (!dirPath.exists()) {
                dirPath.mkdirs();
            }
            String fileName = dirPath.getAbsolutePath() + File.separator + System.currentTimeMillis() + ".amr";

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
        } catch (Exception e) {
            Log.d(TAG, "startMediaRecorder: " + e.getMessage());
            return false;
        }
    }

    private void stopRecorder() {
        recorder.release();
        //recorder.stop();
        recorder = null;
        isRecordStarted = true;
        Intent serviceIntent = new Intent(this, MainActivity.class);
        //serviceIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        serviceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(serviceIntent);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        Log.d(TAG, "onAccessibilityEvent: PackageName " + accessibilityEvent.getPackageName());
        Log.d(TAG, "onAccessibilityEvent: getMode " + audioManager.getMode());
        /*RecordedAudioToFileController audioToFileController = new RecordedAudioToFileController();
         */
        /* if(ForegroundService.isStartRecord && !ForegroundService.isRecordStarted){
            Toast.makeText(this, "start Record", Toast.LENGTH_SHORT).show();
            Intent serviceIntent = new Intent(this, ForegroundService.class);
            serviceIntent.putExtra("inputExtra", "Call Recording");
            ContextCompat.startForegroundService(this, serviceIntent);
        } else {
            Toast.makeText(this, "Record saved", Toast.LENGTH_SHORT).show();
            stopRecorder();
        }*/
        /*if (audioManager.getMode() == AudioManager.MODE_IN_CALL) {
            */
        /*
            //audioToFileController.start();
            if (!isRecordStarted) { //com.catalinagroup.callrecorder
                Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show();
                AudioManager am=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
                assert am != null;
                am.setMode(AudioManager.RINGER_MODE_NORMAL);
                startMediaRecorder(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
            }
        }*/
        /* else if (audioManager.getMode() == AudioManager.MODE_NORMAL) {
            //stopService(new Intent(GlobalAccessibilityService.this, ForegroundService.class));
            //audioToFileController.stop();
            if (isRecordStarted) {
                //Toast.makeText(this, "Record saved", Toast.LENGTH_SHORT).show();
                stopRecorder();
            }

        }*/
        /*if (isRecordStarted) {
        //Toast.makeText(this, "Record saved", Toast.LENGTH_SHORT).show();
        stopRecorder();
    }*/
        /* if(accessibilityEvent.getPackageName().equals("com.android.server.telecom")){
            if (info != null && info.getText() != null) {
                String duration = info.getText().toString();
                String zeroSeconds = String.format("%02d:%02d", new Object[]{Integer.valueOf(0), Integer.valueOf(0)});
                String firstSecond = String.format("%02d:%02d", new Object[]{Integer.valueOf(0), Integer.valueOf(1)});

                Log.d("myaccess", "after calculation - " + zeroSeconds + " --- " + firstSecond + " --- " + duration);
                if (zeroSeconds.equals(duration) || firstSecond.equals(duration)) {
                    Toast.makeText(getApplicationContext(), "Call answered", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onAccessibilityEvent: 11113");1
                    // Your Code goes here
                }
                info.recycle();
            }
            Intent serviceIntent = new Intent(this, ForegroundService.class);
            serviceIntent.putExtra("inputExtra", "Call Recording");
            ContextCompat.startForegroundService(this, serviceIntent);
        }
        if(accessibilityEvent.getPackageName().equals("com.naresh.hubpoc")){
            stopService(new Intent(GlobalAccessibilityService.this, ForegroundService.class));
        }*/
    }

    public GlobalAccessibilityService() {
        super();
        Log.d(TAG, "GlobalAccessibilityService: ");
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Toast.makeText(this, "Service connected", Toast.LENGTH_LONG).show();
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK;
        info.notificationTimeout = 0;
        info.packageNames = new String[]{"com.telebu.hubpoc"};
        setServiceInfo(info);
    }

    @Override
    public boolean bindService(Intent service, ServiceConnection conn, int flags) {
        return super.bindService(service, conn, flags);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    protected boolean onGesture(int gestureId) {
        return super.onGesture(gestureId);
    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        return super.onKeyEvent(event);
    }

    @Override
    public List<AccessibilityWindowInfo> getWindows() {
        return super.getWindows();
    }

    @Override
    public AccessibilityNodeInfo getRootInActiveWindow() {
        return super.getRootInActiveWindow();
    }

    @Override
    public AccessibilityNodeInfo findFocus(int focus) {
        return super.findFocus(focus);
    }

    @Override
    public Object getSystemService(@NonNull String name) {
        return super.getSystemService(name);
    }

    @Override
    public void onInterrupt() {
        Log.d(TAG, "onInterrupt: ");
    }

}