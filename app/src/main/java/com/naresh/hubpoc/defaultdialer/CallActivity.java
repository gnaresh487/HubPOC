package com.naresh.hubpoc.defaultdialer;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.telecom.Call;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.naresh.hubpoc.GlobalAccessibilityService;
import com.naresh.hubpoc.R;
import com.naresh.hubpoc.activity.MainActivity;
import com.naresh.hubpoc.service.ForegroundService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import kotlin.collections.CollectionsKt;


import static com.naresh.hubpoc.defaultdialer.Constants.asString;
import static com.naresh.hubpoc.service.ForegroundService.isRecordStarted;

public class CallActivity extends AppCompatActivity {

    @BindView(R.id.answer)
    Button answer;
    @BindView(R.id.hangup)
    Button hangup;
    @BindView(R.id.callInfo)
    TextView callInfo;
    private Visualizer visualiser;
    private static final int CAPTURE_SIZE = 256;

    private static final String TAG = CallActivity.class.getSimpleName();
    private CompositeDisposable disposables;
    private String number;
    private OngoingCall ongoingCall;
    private Boolean isMuted = false;
    MediaRecorder recorder;
    final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
    public static boolean outgoingCall = false;

    Intent mSpeechRecognizerIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        ButterKnife.bind(this);

        ongoingCall = new OngoingCall();
        disposables = new CompositeDisposable();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        number = Objects.requireNonNull(getIntent().getData()).getSchemeSpecificPart();
        //setUpRecorder();

    }

    /*private void startVisualiser() {
        visualiser = new Visualizer(0);
        visualiser.setDataCaptureListener(this, Visualizer.getMaxCaptureRate(), true, false);
        visualiser.setCaptureSize(CAPTURE_SIZE);
        visualiser.setEnabled(true);
    }

    private void stopVisualiser(){
        if (visualiser != null) {
            visualiser.setEnabled(false);
            visualiser.release();
            visualiser.setDataCaptureListener(null, 0, false, false);
        }
    }*/

    @OnClick(R.id.answer)
    public void onAnswerClicked() {
        ongoingCall.answer();

        /*Log.d(TAG, "onAnswerClicked: record started");
        isRecordStarted = true;
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        serviceIntent.putExtra("inputExtra", "Call Recording");
        ContextCompat.startForegroundService(this, serviceIntent);*/

        //startVisualiser();
        //startSpeechRecognition();
        //mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
        //startMediaRecorder(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
    }

    @OnClick(R.id.hangup)
    public void onHangupClicked() {
        ongoingCall.hangup();
       /* isRecordStarted = false;
        AudioManager am=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
        assert am != null;
        am.setMode(AudioManager.MODE_NORMAL);
        stopService(new Intent(this, ForegroundService.class));*/

        /*AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        assert am != null;
        am.setMode(AudioManager.MODE_NORMAL);*/

        /*AudioManager am=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
        assert am != null;
        am.setMode(AudioManager.RINGER_MODE_NORMAL);*/
        Log.d(TAG, "onHangupClicked: record saved");
        //stopVisualiser();
        //mSpeechRecognizer.stopListening();
        //stopRecorder();
    }

    private void startMediaRecorder(final int audioSource) {
        Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show();
        recorder = new MediaRecorder();
        AudioManager am=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
        assert am != null;
        am.setMode(AudioManager.MODE_NORMAL);

        try {
            File dirPath = new File(getExternalFilesDir(null).getAbsolutePath() + "/tempFiles");

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
              //  Log.e(TAG, "OnInfoListener " + arg1 + "," + arg2);
                //terminateAndEraseFile();
            };
            recorder.setOnInfoListener(infoListener);

            recorder.prepare();
            // Sometimes prepare takes some time to complete
            //Thread.sleep(2000);
            recorder.start();
            //isRecordStarted = true;
        } catch (Exception e) {
            //Log.d(TAG, "startMediaRecorder: " + e.getMessage());
        }
    }

    private void stopRecorder() {
        Toast.makeText(this, "Record saved", Toast.LENGTH_SHORT).show();
        recorder.release();
        //recorder.stop();
        recorder = null;
        //isRecordStarted = false;
        Intent serviceIntent = new Intent(this, MainActivity.class);
        //serviceIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        serviceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(serviceIntent);
    }

    public void muteCall(){
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if(!isMuted){

            if((audioManager.getMode()== AudioManager.MODE_IN_CALL)||(audioManager.getMode()== AudioManager.MODE_IN_COMMUNICATION)){
                audioManager.setMicrophoneMute(true);
            }
            isMuted = true;

        }else{

            if((audioManager.getMode()== AudioManager.MODE_IN_CALL)||(audioManager.getMode()== AudioManager.MODE_IN_COMMUNICATION)){
                audioManager.setMicrophoneMute(false);
            }
            isMuted = false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        assert updateUi(-1) != null;
        disposables.add(
                OngoingCall.state
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Exception {
                                updateUi(integer);
                            }
                        }));

        disposables.add(
                OngoingCall.state
                        .filter(new Predicate<Integer>() {
                            @Override
                            public boolean test(Integer integer) throws Exception {
                                return integer == Call.STATE_DISCONNECTED;
                            }
                        })
                        .delay(1, TimeUnit.SECONDS)
                        .firstElement()
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Exception {
                                finish();
                            }
                        }));

    }

    @SuppressLint("SetTextI18n")
    private Consumer<? super Integer> updateUi(Integer state) {

        callInfo.setText(asString(state) + "\n" + number);

        if (state != Call.STATE_RINGING) {
            answer.setVisibility(View.GONE);
        } else answer.setVisibility(View.VISIBLE);

        if (CollectionsKt.listOf(new Integer[]{
                Call.STATE_DIALING,
                Call.STATE_RINGING,
                Call.STATE_ACTIVE}).contains(state)) {
            hangup.setVisibility(View.VISIBLE);
        } else
            hangup.setVisibility(View.GONE);

        return null;
    }

    @Override
    protected void onStop() {
        super.onStop();
        disposables.clear();
    }

    public static void start(Context context, Call call) {
        Intent intent = new Intent(context, CallActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .setData(call.getDetails().getHandle());
        context.startActivity(intent);
    }

    public void startSpeechRecognition() {
        // Fire an intent to start the speech recognition activity.

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        // secret parameters that when added provide audio url in the result
        intent.putExtra("android.speech.extra.GET_AUDIO_FORMAT", "audio/AMR");
        intent.putExtra("android.speech.extra.GET_AUDIO", true);

        startActivityForResult(intent, 2020);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2020) {
            Bundle bundle = data.getExtras();
            ArrayList<String> matches = bundle.getStringArrayList(RecognizerIntent.EXTRA_RESULTS);
            // the recording url is in getData:
            Uri audioUri = data.getData();
            Log.d("CallActivity", "onActivityResult: audioUri "+audioUri);
            ContentResolver contentResolver = getContentResolver();
            try {
                //InputStream filestream = contentResolver.openInputStream(audioUri);
                InputStream filestream = contentResolver.openInputStream(audioUri);
                byte[] buffer = new byte[filestream.available()];
                filestream.read(buffer);
                File dirPath = new File(getExternalFilesDir(null).getAbsolutePath() + "/tempFiles");

                if (!dirPath.exists()) {
                    dirPath.mkdirs();
                }
                String fileName = dirPath.getAbsolutePath() + File.separator + System.currentTimeMillis() + ".amr";
                OutputStream outStream = new FileOutputStream(fileName);
                outStream.write(buffer);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setUpRecorder(){
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        /*mSpeechRecognizerIntent.putExtra("android.speech.extra.GET_AUDIO_FORMAT",
                "audio/AMR");
        mSpeechRecognizerIntent.putExtra("android.speech.extra.GET_AUDIO", true);*/
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                  Locale.getDefault());

        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

                Log.d(TAG, "onReadyForSpeech: 1");
            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {
                Log.d(TAG, "onBufferReceived: 2");
            }

            @Override
            public void onEndOfSpeech() {

                Log.d(TAG, "onEndOfSpeech: 2");
            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                //getting all the matches
                ArrayList<String> matches = bundle
                        .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                Log.d(TAG, "onResults: 4 "+matches);
                //displaying the first match
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

    }
}
