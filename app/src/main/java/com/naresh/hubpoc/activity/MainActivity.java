package com.naresh.hubpoc.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.Settings;
import android.telecom.TelecomManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.naresh.hubpoc.CallLogsAdapter;
import com.naresh.hubpoc.CallLogsModel;
import com.naresh.hubpoc.R;
import com.naresh.hubpoc.TelephonyInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.READ_CALL_LOG;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.telecom.TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String MY_PREFS_NAME = "pref_app";
    private static final String SIM_SLOT_ID = "sim_slot";
    RecyclerView callListRv;
    CallLogsAdapter callLogsAdapter;
    ArrayList<CallLogsModel> callLogsList;
    String sim1PhoneNumber = "", sim2PhoneNumber = "";
    String sim1IccID = "", sim2IccID = "";
    String simIccID = "";
    String[] projection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        callListRv = findViewById(R.id.call_list_rv);
        projection = new String[]{
                CallLog.Calls._ID,
                CallLog.Calls.NUMBER,
                CallLog.Calls.DATE,
                CallLog.Calls.DURATION,
                CallLog.Calls.TYPE,
                CallLog.Calls.CACHED_FORMATTED_NUMBER,
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.NUMBER_PRESENTATION,
                CallLog.Calls.CACHED_NORMALIZED_NUMBER,
                CallLog.Calls.PHONE_ACCOUNT_COMPONENT_NAME,
                CallLog.Calls.PHONE_ACCOUNT_ID,
                CallLog.Calls.COUNTRY_ISO
        };
        // CallLog.Calls.VIA_NUMBER
        callLogsList = new ArrayList<>();
        callLogsAdapter = new CallLogsAdapter(this, phoneNumber -> {
            if (checkMakeCallPermission()) {
                String dial = "tel:" + phoneNumber;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            } else {
                requestMakeCallPermissions();
            }
        });
        callListRv.setLayoutManager(new LinearLayoutManager(this));
        callListRv.setAdapter(callLogsAdapter);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        simIccID = prefs.getString(SIM_SLOT_ID, "");
        Log.d(TAG, "onCreate: prefs "+simIccID);
        if(!checkRecordCallPermission() || !checkStoragePermission() ||
                !checkReadPhoneStatePermission() || !checkCallLogsPermission()) {
            requestAllPermissions();
        } else {
            if(!simIccID.equals("")){
                getCallLogs(projection);
            } else {
                getSimSlotNumber();
                /*if(!simIccID.equals("")) {
                    getCallLogs(projection);
                }*/
            }
        }

        /*if(checkRecordCallPermission()){

        } else {
            requestRecordPermission();
        }
        if(checkStoragePermission()){

        } else {
            requestStoragePermission();
        }

        if(checkReadPhoneStatePermission()){
            getSimSlotNumber();
            *//*TelephonyManager telephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (telephony != null) {
                Log.d(TAG, "onCreate: telephony "+telephony.getLine1Number());
            }*//*
        } else {
            requestPhoneStatePermission();
        }

        if (checkCallLogsPermission()) {
            //check();
            getCallLogs(projection);
        } else {
            requestReadCallLogsPermission();
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.defaultApp:
                offerReplacingDefaultDialer();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
}

    private void offerReplacingDefaultDialer() {
        TelecomManager telecomManager = (TelecomManager) getSystemService(TELECOM_SERVICE);

        /*if (!getPackageName().equals(telecomManager.getDefaultDialerPackage())) {

        }*/

        Intent intent = new Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS)
                .putExtra(EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, getPackageName());
        startActivity(intent);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ALL_PERMISSIONS:
                if (grantResults.length > 0) {
                    if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED && grantResults[3] == PackageManager.PERMISSION_GRANTED){
                        getSimSlotNumber();
                        //getCallLogs(projection);
                    } else {
                        finish();
                    }
/*
                    if(grantResults[1] == PackageManager.PERMISSION_GRANTED){
                        getCallLogs(projection);
                    } else {
                        requestReadCallLogsPermission();
                    }

                    if(grantResults[2] == PackageManager.PERMISSION_GRANTED){
                        //getCallLogs(projection);
                    } else {
                        requestRecordPermission();
                    }

                    if(grantResults[3] == PackageManager.PERMISSION_GRANTED){
                        //getCallLogs(projection);
                    } else {
                        requestStoragePermission();
                    }*/
                }
                break;
            /*case PHONE_STATE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    getSimSlotNumber();
                } else {
                    // Permission Denied
                }
                break;
            case CALL_LOG:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    getCallLogs(projection);
                } else {
                    // Permission Denied
                }
                break;*/
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void getCallLogs(String[] projection) {
        //Fetches the complete call log in descending order. i.e recent calls appears first.

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Cursor c = getApplicationContext().getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, null,
                null, CallLog.Calls.DATE + " DESC");

        if (c != null && c.getCount() > 0) {
            c.moveToFirst();

            do {
                String simSubId = c.getString(c.getColumnIndexOrThrow(CallLog.Calls.PHONE_ACCOUNT_ID));
                Log.d(TAG, "getCallLogs: simSubId "+simSubId +" simIccID "+simIccID);
                if(simSubId != null && simSubId.contains(simIccID)) {
                    String callerID = c.getString(c.getColumnIndex(CallLog.Calls._ID));
                    String callerNumber = c.getString(c.getColumnIndex(CallLog.Calls.NUMBER));
                    String userNumber = c.getString(c.getColumnIndex(CallLog.Calls.CACHED_FORMATTED_NUMBER));
                    long callDateAndTime = c.getLong(c.getColumnIndex(CallLog.Calls.DATE));
                    Date callDate = new Date(callDateAndTime);
                    String timeStamp = new SimpleDateFormat("HH:mm").format(callDate);
                    long time = callDate.getHours();
                    long callDuration = c.getLong(c.getColumnIndex(CallLog.Calls.DURATION));
                    int callType = c.getInt(c.getColumnIndex(CallLog.Calls.TYPE));
                    String callerName = c.getString(c.getColumnIndex(CallLog.Calls.CACHED_NAME));
                    String callerCountry = c.getString(c.getColumnIndex(CallLog.Calls.COUNTRY_ISO));

                /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    int viaNumber = c.getInt(c.getColumnIndex(CallLog.Calls.VIA_NUMBER));
                }
*/
                /*if (callType == CallLog.Calls.INCOMING_TYPE) {
                    //incoming call
                } else if (callType == CallLog.Calls.OUTGOING_TYPE) {
                    //outgoing call
                } else if (callType == CallLog.Calls.MISSED_TYPE) {
                    //missed call
                }*/
                    Log.d(TAG, "getCallLogs:  " + simSubId + "," + sim1PhoneNumber);

                    int simSlot;
                    if (simSubId.contains(sim1IccID)) {
                        simSlot = 1;
                        Log.d(TAG, "getCallLogs: sim1 " + sim1PhoneNumber);
                    } else if (simSubId.contains(sim2IccID)) {
                        simSlot = 2;
                        Log.d(TAG, "getCallLogs: sim2 " + sim2PhoneNumber);
                    } else {
                        simSlot = 0;
                        Log.d(TAG, "getCallLogs: sim not found");
                    }

                    CallLogsModel model = new CallLogsModel();
                    model.setCallerID(callerID);
                    model.setCallerName(callerName);
                    model.setCallerNumber(callerNumber);
                    model.setCountryCode(callerCountry);
                    model.setUserNumber(userNumber);
                    model.setCallDateAndTime(time);
                    model.setCallTime(timeStamp);
                    model.setCallDuration(callDuration);
                    model.setCallType(callType);
                    model.setSimSlot(simSlot);

                    callLogsList.add(model);
                }

            } while (c.moveToNext());
        }

        Log.d(TAG, "getCallLogs: callLogsList size " + callLogsList.size());
        callLogsAdapter.setData(callLogsList);
    }

    private void requestPhoneStatePermission() {
        ActivityCompat.requestPermissions(this, new
                String[]{READ_PHONE_STATE}, PHONE_STATE);
    }

    private void requestRecordPermission() {
        ActivityCompat.requestPermissions(this, new
                String[]{RECORD_AUDIO}, RECORD_CALL);
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, new
                String[]{WRITE_EXTERNAL_STORAGE}, STORAGE);
    }

    private void requestAllPermissions() {
        ActivityCompat.requestPermissions(this, new
                String[]{READ_PHONE_STATE, READ_CALL_LOG, RECORD_AUDIO, WRITE_EXTERNAL_STORAGE}, ALL_PERMISSIONS);
    }

    private void requestMakeCallPermissions() {
        ActivityCompat.requestPermissions(this, new
                String[]{CALL_PHONE}, MAKE_CALL);
    }

    private void requestReadCallLogsPermission() {
        ActivityCompat.requestPermissions(this, new
                String[]{READ_CALL_LOG}, CALL_LOG);
    }

    private void getSimSlotNumber() {
        Log.d("getSimSlotNumber","getSimSlotNumber");
        SubscriptionManager localSubscriptionManager = SubscriptionManager.from(this);
        if (Build.VERSION.SDK_INT > 22) {
            Log.d("getSimSlotNumber","getSimSlotNumber1");
            //for dual sim mobile

            if (ActivityCompat.checkSelfPermission(this, READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            if (localSubscriptionManager.getActiveSubscriptionInfoCount() > 1) {
                Log.d("getSimSlotNumber","getSimSlotNumber2");
                //if there are two sims in dual sim mobile
                List localList = localSubscriptionManager.getActiveSubscriptionInfoList();
                SubscriptionInfo simInfo1 = (SubscriptionInfo) localList.get(0);
                SubscriptionInfo simInfo2 = (SubscriptionInfo) localList.get(1);

                sim1PhoneNumber = String.valueOf(simInfo1.getNumber());
                sim2PhoneNumber = String.valueOf(simInfo2.getNumber());

                sim1IccID = simInfo1.getIccId();
                sim2IccID = simInfo2.getIccId();

                makeDialogForMultipleNumbers(simInfo1, simInfo2);
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Log.d(TAG, "getSimSlotNumber: "+String.valueOf(simInfo1.getCardId()));
                    Log.d(TAG, "getSimSlotNumber: "+String.valueOf(simInfo1.getCarrierId()));
                    Log.d(TAG, "getSimSlotNumber: "+String.valueOf(simInfo1.getGroupUuid()));
                    Log.d(TAG, "getSimSlotNumber: "+String.valueOf(simInfo1.getMccString()));
                    Log.d(TAG, "getSimSlotNumber: "+String.valueOf(simInfo1.getMncString()));
                }*/

                Log.d(TAG, "getSimSlotNumber: iccid1 "+String.valueOf(simInfo1.getIccId()));
                Log.d(TAG, "getSimSlotNumber: iccid2 "+String.valueOf(simInfo2.getIccId()));
                Log.d(TAG, "getSimSlotNumber: Simslot1 "+String.valueOf(simInfo1.getSimSlotIndex()));
                Log.d(TAG, "getSimSlotNumber: Simslot2 "+String.valueOf(simInfo2.getSimSlotIndex()));

            } else {
                //if there is 1 sim in dual sim mobile
                TelephonyManager tManager = (TelephonyManager) getBaseContext()
                        .getSystemService(Context.TELEPHONY_SERVICE);

                sim1PhoneNumber = tManager.getNetworkOperatorName();
                if(localSubscriptionManager.getActiveSubscriptionInfoCount()>0){
                    Log.d("getSimSlotNumber","getSimSlotNumber3:"+localSubscriptionManager.getActiveSubscriptionInfoCount());
                    List localList = localSubscriptionManager.getActiveSubscriptionInfoList();
                    SubscriptionInfo simInfo1 = (SubscriptionInfo) localList.get(0);
                    sim1PhoneNumber = String.valueOf(simInfo1.getNumber());
                    simIccID = simInfo1.getIccId();
                    Log.d(TAG, "getSimSlotNumber: iccid1 "+simInfo1.getIccId()+","+simInfo1.getNumber()+"....");
                    Log.d(TAG, "getSimSlotNumber: Simslot1 "+String.valueOf(simInfo1.getSimSlotIndex()));

                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putString(SIM_SLOT_ID, simIccID);
                    editor.apply();
                    getCallLogs(projection);

                }
            }

        }else{
            Log.d("getSimSlotNumber","getSimSlotNumber4");
            //below android version 22
            TelephonyManager tManager = (TelephonyManager) getBaseContext()
                    .getSystemService(Context.TELEPHONY_SERVICE);

            sim1PhoneNumber = tManager.getNetworkOperatorName();
            if(localSubscriptionManager.getActiveSubscriptionInfoCount()>0){
                Log.d("getSimSlotNumber","getSimSlotNumber3:"+localSubscriptionManager.getActiveSubscriptionInfoCount());
                List localList = localSubscriptionManager.getActiveSubscriptionInfoList();
                SubscriptionInfo simInfo1 = (SubscriptionInfo) localList.get(0);
                sim1PhoneNumber = String.valueOf(simInfo1.getNumber());
                simIccID = simInfo1.getIccId();
                Log.d(TAG, "getSimSlotNumber: iccid1 "+simInfo1.getIccId()+","+simInfo1.getNumber()+"....");
                Log.d(TAG, "getSimSlotNumber: Simslot1 "+String.valueOf(simInfo1.getSimSlotIndex()));
            }
        }

    }

    private void check(){
        TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(this);

        String imeiSIM1 = telephonyInfo.getImsiSIM1();
        String imeiSIM2 = telephonyInfo.getImsiSIM2();

        boolean isSIM1Ready = telephonyInfo.isSIM1Ready();
        boolean isSIM2Ready = telephonyInfo.isSIM2Ready();

        boolean isDualSIM = telephonyInfo.isDualSIM();

        Log.d(TAG, "check: IME1 : " + imeiSIM1 + "\n" +
                " IME2 : " + imeiSIM2 + "\n" +
                " IS DUAL SIM : " + isDualSIM + "\n" +
                " IS SIM1 READY : " + isSIM1Ready + "\n" +
                " IS SIM2 READY : " + isSIM2Ready + "\n");
    }

    private void makeDialogForMultipleNumbers(SubscriptionInfo simInfo1, SubscriptionInfo simInfo2) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.sim_select_dialog);
        LinearLayout sim1Layout = dialog.findViewById(R.id.sim1_layout);
        LinearLayout sim2Layout = dialog.findViewById(R.id.sim2_layout);
        TextView sim1Text = dialog.findViewById(R.id.sim1_text);
        TextView sim2Text = dialog.findViewById(R.id.sim2_text);
        WindowManager.LayoutParams lWindowParams = new WindowManager.LayoutParams();
        lWindowParams.copyFrom(dialog.getWindow().getAttributes());
        lWindowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        lWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lWindowParams);

        /*if(!simInfo1.getNumber().trim().equals(""))
        sim1Text.setText(String.valueOf(simInfo1.getNumber()));

        if(!simInfo2.getNumber().trim().equals(""))
        sim2Text.setText(String.valueOf(simInfo2.getNumber()));*/

        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

        sim1Layout.setOnClickListener(v -> {
            Log.d(TAG, "makeDialogForMultipleNumbers: sim1Layout");
            simIccID = simInfo1.getIccId();

            Log.d(TAG, "onCreate: pref 1 "+simIccID);
            editor.putString(SIM_SLOT_ID, simIccID);
            editor.apply();

            getCallLogs(projection);
            dialog.dismiss();
        });

        sim2Layout.setOnClickListener(v -> {
            Log.d(TAG, "makeDialogForMultipleNumbers: sim2Layout");
            simIccID = simInfo2.getIccId();

            Log.d(TAG, "onCreate: pref 1 "+simIccID);
            editor.putString(SIM_SLOT_ID, simIccID);
            editor.apply();
            getCallLogs(projection);
            dialog.dismiss();
        });

        dialog.show();
    }

}
