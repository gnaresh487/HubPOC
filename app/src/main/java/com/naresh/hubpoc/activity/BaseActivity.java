package com.naresh.hubpoc.activity;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.READ_CALL_LOG;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_SECURE_SETTINGS;

public class BaseActivity extends AppCompatActivity {

    public static final int CALL_LOG = 100;
    public static final int PHONE_STATE = 101;
    public static final int RECORD_CALL = 102;
    public static final int STORAGE = 103;
    public static final int ALL_PERMISSIONS = 104;
    public static final int MAKE_CALL = 105;
    public static final int ACCESSIBILITY_PERMISSION = 105;
    public static final int WRITE_ACCESSIBILITY_PERMISSION = 106;
    public static final int INCOMING_CALL = 1;
    public static final int OUTGOING_CALL = 2;
    public static final int MISSED_CALL = 3;
    private static final String TAG = BaseActivity.class.getSimpleName();
    private ProgressDialog mProgressDialog;

    public boolean checkCallLogsPermission() {
        return ContextCompat.checkSelfPermission(getApplicationContext(), READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED;
    }

    public boolean checkReadPhoneStatePermission() {
        return ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;
    }

    public boolean checkMakeCallPermission() {
        return ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED;
    }

    public boolean checkRecordCallPermission() {
        return ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
    }

    public boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public boolean checkWriteAccessibilityPermission() {
        return ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_SECURE_SETTINGS) == PackageManager.PERMISSION_GRANTED;
    }

    public boolean checkAudioRecordPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }

    public boolean requestPermission(String permission) {
        boolean isGranted = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
        if (!isGranted) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{permission},
                    CALL_LOG);
        }
        return isGranted;
    }

    public void isPermissionGranted(boolean isGranted, String permission) {

    }

    public void makeFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CALL_LOG:
                isPermissionGranted(grantResults[0] == PackageManager.PERMISSION_GRANTED, permissions[0]);
                break;
        }
    }
*/

    protected void showLoading(@NonNull String message) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(message);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    protected void hideLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    protected void showSnackbar(@NonNull String message) {
        View view = findViewById(android.R.id.content);
        if (view != null) {
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }
}
