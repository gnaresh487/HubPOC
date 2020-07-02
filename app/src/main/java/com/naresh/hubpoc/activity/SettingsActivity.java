package com.naresh.hubpoc.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.naresh.hubpoc.R;
import com.naresh.hubpoc.SharedPrefUtils;
import com.naresh.hubpoc.databinding.ActivitySettingsBinding;

import static com.naresh.hubpoc.activity.MainActivity.AUDIO_ENCODER;
import static com.naresh.hubpoc.activity.MainActivity.AUDIO_FORMAT;
import static com.naresh.hubpoc.activity.MainActivity.AUDIO_SOURCE;
import static com.naresh.hubpoc.activity.MainActivity.OUTPUT_FORMAT;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivitySettingsBinding settingsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingsBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        //setContentView(R.layout.activity_settings);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.mp3:
                SharedPrefUtils.saveData(this, AUDIO_FORMAT, ".mp3");
                Toast.makeText(this, "mp3", Toast.LENGTH_SHORT).show();
                break;
            case R.id.amr:
                SharedPrefUtils.saveData(this, AUDIO_FORMAT, ".amr");
                Toast.makeText(this, "amr", Toast.LENGTH_SHORT).show();
                break;


            case R.id.rec_default:
                SharedPrefUtils.saveData(this, AUDIO_SOURCE, MediaRecorder.AudioSource.DEFAULT);
                Toast.makeText(this, "0", Toast.LENGTH_SHORT).show();
                break;
            case R.id.mic:
                SharedPrefUtils.saveData(this, AUDIO_SOURCE, MediaRecorder.AudioSource.MIC);
                Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.voice_uplink:
                SharedPrefUtils.saveData(this, AUDIO_SOURCE, MediaRecorder.AudioSource.VOICE_UPLINK);
                Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.voice_downlink:
                SharedPrefUtils.saveData(this, AUDIO_SOURCE, MediaRecorder.AudioSource.VOICE_DOWNLINK);
                Toast.makeText(this, "3", Toast.LENGTH_SHORT).show();
                break;
            case R.id.camcorder:
                SharedPrefUtils.saveData(this, AUDIO_SOURCE, MediaRecorder.AudioSource.CAMCORDER);
                Toast.makeText(this, "4", Toast.LENGTH_SHORT).show();
                break;
            case R.id.voice_call:
                SharedPrefUtils.saveData(this, AUDIO_SOURCE, MediaRecorder.AudioSource.VOICE_CALL);
                Toast.makeText(this, "5", Toast.LENGTH_SHORT).show();
                break;
            case R.id.voice_communication:
                SharedPrefUtils.saveData(this, AUDIO_SOURCE, MediaRecorder.AudioSource.VOICE_COMMUNICATION);
                Toast.makeText(this, "6", Toast.LENGTH_SHORT).show();
                break;
            case R.id.remote_submix:
                SharedPrefUtils.saveData(this, AUDIO_SOURCE, MediaRecorder.AudioSource.REMOTE_SUBMIX);
                Toast.makeText(this, "7", Toast.LENGTH_SHORT).show();
                break;
            case R.id.unprocessed:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    SharedPrefUtils.saveData(this, AUDIO_SOURCE, MediaRecorder.AudioSource.UNPROCESSED);
                }
                Toast.makeText(this, "8", Toast.LENGTH_SHORT).show();
                break;
            case R.id.voice_performance:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    SharedPrefUtils.saveData(this, AUDIO_SOURCE, MediaRecorder.AudioSource.VOICE_PERFORMANCE);
                }
                Toast.makeText(this, "9", Toast.LENGTH_SHORT).show();
                break;


            case R.id.aac:
                SharedPrefUtils.saveData(this, AUDIO_ENCODER, MediaRecorder.AudioEncoder.AAC);
                Toast.makeText(this, "AAC", Toast.LENGTH_SHORT).show();
                break;
            case R.id.aac_eld:
                SharedPrefUtils.saveData(this, AUDIO_ENCODER, MediaRecorder.AudioEncoder.AAC_ELD);
                Toast.makeText(this, "AAC_ELD", Toast.LENGTH_SHORT).show();
                break;
            case R.id.amr_nb:
                SharedPrefUtils.saveData(this, AUDIO_ENCODER, MediaRecorder.AudioEncoder.AMR_NB);
                Toast.makeText(this, "AMR_NB", Toast.LENGTH_SHORT).show();
                break;
            case R.id.amr_wb:
                SharedPrefUtils.saveData(this, AUDIO_ENCODER, MediaRecorder.AudioEncoder.AMR_WB);
                Toast.makeText(this, "AMR_WB", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ae_default:
                SharedPrefUtils.saveData(this, AUDIO_ENCODER, MediaRecorder.AudioEncoder.DEFAULT);
                Toast.makeText(this, "DEFAULT", Toast.LENGTH_SHORT).show();
                break;
            case R.id.he_aac:
                SharedPrefUtils.saveData(this, AUDIO_ENCODER, MediaRecorder.AudioEncoder.HE_AAC);
                Toast.makeText(this, "HE_AAC", Toast.LENGTH_SHORT).show();
                break;
            case R.id.opus:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    SharedPrefUtils.saveData(this, AUDIO_ENCODER, MediaRecorder.AudioEncoder.OPUS);
                }
                Toast.makeText(this, "OPUS", Toast.LENGTH_SHORT).show();
                break;
            case R.id.vorbis:
                SharedPrefUtils.saveData(this, AUDIO_ENCODER, MediaRecorder.AudioEncoder.VORBIS);
                Toast.makeText(this, "VORBIS", Toast.LENGTH_SHORT).show();
                break;


            case R.id.of_aac_adts:
                SharedPrefUtils.saveData(this, OUTPUT_FORMAT, MediaRecorder.OutputFormat.AAC_ADTS);
                Toast.makeText(this, "AAC_ADTS", Toast.LENGTH_SHORT).show();
                break;
            case R.id.of_amr_nb:
                SharedPrefUtils.saveData(this, OUTPUT_FORMAT, MediaRecorder.OutputFormat.AMR_NB);
                Toast.makeText(this, "AMR_NB", Toast.LENGTH_SHORT).show();
                break;
            case R.id.of_amr_wb:
                SharedPrefUtils.saveData(this, OUTPUT_FORMAT, MediaRecorder.OutputFormat.AMR_WB);
                Toast.makeText(this, "AMR_WB", Toast.LENGTH_SHORT).show();
                break;
            case R.id.of_default:
                SharedPrefUtils.saveData(this, OUTPUT_FORMAT, MediaRecorder.OutputFormat.DEFAULT);
                Toast.makeText(this, "DEFAULT", Toast.LENGTH_SHORT).show();
                break;
            case R.id.of_mpeg_2_ts:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    SharedPrefUtils.saveData(this, OUTPUT_FORMAT, MediaRecorder.OutputFormat.MPEG_2_TS);
                }
                Toast.makeText(this, "MPEG_2_TS", Toast.LENGTH_SHORT).show();
                break;
            case R.id.of_three_gpp:
                SharedPrefUtils.saveData(this, OUTPUT_FORMAT, MediaRecorder.OutputFormat.THREE_GPP);
                Toast.makeText(this, "THREE_GPP", Toast.LENGTH_SHORT).show();
                break;
            case R.id.of_mpeg4:
                SharedPrefUtils.saveData(this, OUTPUT_FORMAT, MediaRecorder.OutputFormat.MPEG_4);
                Toast.makeText(this, "MPEG_4", Toast.LENGTH_SHORT).show();
                break;
            case R.id.of_ogg:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    SharedPrefUtils.saveData(this, OUTPUT_FORMAT, MediaRecorder.OutputFormat.OGG);
                }
                Toast.makeText(this, "OGG", Toast.LENGTH_SHORT).show();
                break;
            case R.id.of_webm:
                SharedPrefUtils.saveData(this, OUTPUT_FORMAT, MediaRecorder.OutputFormat.WEBM);
                Toast.makeText(this, "WEBM", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
