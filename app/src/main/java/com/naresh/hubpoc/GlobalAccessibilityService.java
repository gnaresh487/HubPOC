package com.naresh.hubpoc;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.naresh.hubpoc.service.ForegroundService;

import java.util.List;

public class GlobalAccessibilityService extends AccessibilityService {

    private static final String TAG = GlobalAccessibilityService.class.getSimpleName();
    private AudioManager audioManager;

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

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        Log.d(TAG, "onAccessibilityEvent: PackageName "+ accessibilityEvent.getPackageName());
        Log.d(TAG, "onAccessibilityEvent: getMode "+ audioManager.getMode() );

        if(audioManager.getMode() == AudioManager.MODE_IN_CALL || audioManager.getMode() == AudioManager.MODE_RINGTONE){
            Intent serviceIntent = new Intent(this, ForegroundService.class);
            serviceIntent.putExtra("inputExtra", "Call Recording");
            ContextCompat.startForegroundService(this, serviceIntent);
        } else if(audioManager.getMode() == AudioManager.MODE_NORMAL){
            stopService(new Intent(GlobalAccessibilityService.this, ForegroundService.class));
        }

       /* if(accessibilityEvent.getPackageName().equals("com.android.server.telecom")){
            if (info != null && info.getText() != null) {
                String duration = info.getText().toString();
                String zeroSeconds = String.format("%02d:%02d", new Object[]{Integer.valueOf(0), Integer.valueOf(0)});
                String firstSecond = String.format("%02d:%02d", new Object[]{Integer.valueOf(0), Integer.valueOf(1)});

                Log.d("myaccess", "after calculation - " + zeroSeconds + " --- " + firstSecond + " --- " + duration);
                if (zeroSeconds.equals(duration) || firstSecond.equals(duration)) {
                    Toast.makeText(getApplicationContext(), "Call answered", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onAccessibilityEvent: 11113");
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
        Toast.makeText(this,"Service connected",Toast.LENGTH_LONG).show();
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