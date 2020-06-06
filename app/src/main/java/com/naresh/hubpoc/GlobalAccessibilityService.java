package com.naresh.hubpoc;

import android.accessibilityservice.AccessibilityService;
import android.media.AudioManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;

import androidx.annotation.NonNull;

import java.util.List;

public class GlobalAccessibilityService extends AccessibilityService {

    private static final String TAG = GlobalAccessibilityService.class.getSimpleName();

    private AudioManager audioManager =
            (AudioManager) getSystemService(AUDIO_SERVICE);

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: ");
        super.onCreate();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        Log.d(TAG, "onAccessibilityEvent: ");
       /* AccessibilityNodeInfo interactedNodeInfo =
                accessibilityEvent.getSource();
        if (interactedNodeInfo.getText().equals("Increase volume")) {
            audioManager.adjustStreamVolume(AudioManager.STREAM_ACCESSIBILITY,
                    ADJUST_RAISE, 0);
        }*/
    }

    public GlobalAccessibilityService() {
        super();
        Log.d(TAG, "GlobalAccessibilityService: ");
    }

    @Override
    protected void onServiceConnected() {
        Log.d(TAG, "onServiceConnected: 1");
        /*Toast.makeText(this, "Service Connected", Toast.LENGTH_SHORT).show();
        //super.onServiceConnected();
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
        info.eventTypes=AccessibilityEvent.TYPES_ALL_MASK;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK;
        info.notificationTimeout = 100;
        info.packageNames = null;
        setServiceInfo(info);*/
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