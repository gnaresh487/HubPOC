package com.naresh.hubpoc.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.naresh.hubpoc.R;
import com.naresh.hubpoc.SharedPrefUtils;
import com.naresh.hubpoc.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivitySettingsBinding settingsBinding;
    public static final String AUDIO_SOURCE = "AudioSource";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingsBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        //setContentView(R.layout.activity_settings);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rec_default:
                SharedPrefUtils.saveData(this, AUDIO_SOURCE,MediaRecorder.AudioSource.DEFAULT);
                Toast.makeText(this, "0", Toast.LENGTH_SHORT).show();
                break;
            case R.id.mic:
                SharedPrefUtils.saveData(this, AUDIO_SOURCE,MediaRecorder.AudioSource.MIC);
                Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.voice_uplink:
                SharedPrefUtils.saveData(this, AUDIO_SOURCE,MediaRecorder.AudioSource.VOICE_UPLINK);
                Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.voice_downlink:
                SharedPrefUtils.saveData(this, AUDIO_SOURCE,MediaRecorder.AudioSource.VOICE_DOWNLINK);
                Toast.makeText(this, "3", Toast.LENGTH_SHORT).show();
                break;
            case R.id.camcorder:
                SharedPrefUtils.saveData(this, AUDIO_SOURCE,MediaRecorder.AudioSource.CAMCORDER);
                Toast.makeText(this, "4", Toast.LENGTH_SHORT).show();
                break;
            case R.id.voice_call:
                SharedPrefUtils.saveData(this, AUDIO_SOURCE,MediaRecorder.AudioSource.VOICE_CALL);
                Toast.makeText(this, "5", Toast.LENGTH_SHORT).show();
                break;
            case R.id.voice_communication:
                SharedPrefUtils.saveData(this, AUDIO_SOURCE,MediaRecorder.AudioSource.VOICE_COMMUNICATION);
                Toast.makeText(this, "6", Toast.LENGTH_SHORT).show();
                break;
            case R.id.remote_submix:
                SharedPrefUtils.saveData(this, AUDIO_SOURCE,MediaRecorder.AudioSource.REMOTE_SUBMIX);
                Toast.makeText(this, "7", Toast.LENGTH_SHORT).show();
                break;
            case R.id.unprocessed:
                SharedPrefUtils.saveData(this, AUDIO_SOURCE,MediaRecorder.AudioSource.UNPROCESSED);
                Toast.makeText(this, "8", Toast.LENGTH_SHORT).show();
                break;
            case R.id.voice_performance:
                SharedPrefUtils.saveData(this, AUDIO_SOURCE,MediaRecorder.AudioSource.VOICE_PERFORMANCE);
                Toast.makeText(this, "9", Toast.LENGTH_SHORT).show();
                break;

        }
    }
}
