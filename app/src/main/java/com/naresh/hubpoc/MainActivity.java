package com.naresh.hubpoc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.Manifest.permission.READ_CALL_LOG;
import static android.Manifest.permission.READ_PHONE_STATE;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    RecyclerView callListRv;
    CallLogsAdapter callLogsAdapter;
    ArrayList<CallLogsModel> callLogsList;
    String sim1, sim2;
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
                CallLog.Calls.COUNTRY_ISO,
                CallLog.Calls.VIA_NUMBER
        };
        callLogsList = new ArrayList<>();
        callLogsAdapter = new CallLogsAdapter(this);
        callListRv.setLayoutManager(new LinearLayoutManager(this));
        callListRv.setAdapter(callLogsAdapter);
        if(checkReadPhoneStatePermission()){
            TelephonyManager telephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            Log.d(TAG, "onCreate: telephony "+telephony.getLine1Number());
        } else {
            requestPhoneStatePermission();
        }

        if (checkCallLogsPermission()) {
            check();
            getSimSlotNumber();
            getCallLogs(projection);
        } else {
            requestReadCallLogsPermission();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CALL_LOG && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getSimSlotNumber();
            getCallLogs(projection);
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
                String callerID = c.getString(c.getColumnIndex(CallLog.Calls._ID));
                String callerNumber = c.getString(c.getColumnIndex(CallLog.Calls.NUMBER));
                String userNumber = c.getString(c.getColumnIndex(CallLog.Calls.CACHED_FORMATTED_NUMBER));
                String simNumber = c.getString(c.getColumnIndexOrThrow("subscription_id"));

                long callDateandTime = c.getLong(c.getColumnIndex(CallLog.Calls.DATE));
                Date callDate = new Date(callDateandTime);
                long time = callDate.getHours();
                long callDuration = c.getLong(c.getColumnIndex(CallLog.Calls.DURATION));
                int callType = c.getInt(c.getColumnIndex(CallLog.Calls.TYPE));
                String callerName = c.getString(c.getColumnIndex(CallLog.Calls.CACHED_NAME));
                String callerCountry = c.getString(c.getColumnIndex(CallLog.Calls.COUNTRY_ISO));
                String PHONE_ACCOUNT_COMPONENT_NAME = c.getString(c.getColumnIndex(CallLog.Calls.PHONE_ACCOUNT_COMPONENT_NAME));
                String PHONE_ACCOUNT_ID = c.getString(c.getColumnIndex(CallLog.Calls.PHONE_ACCOUNT_ID));

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    int VIA_NUMBER = c.getInt(c.getColumnIndex(CallLog.Calls.VIA_NUMBER));
                    Log.d(TAG, "getCallLogs: VIA_NUMBER " + VIA_NUMBER);
                }

                /*Log.d(TAG, "getCallLogs: simNumber sim1 "+ sim1);
                Log.d(TAG, "getCallLogs: simNumber sim2 "+ sim2);*/
                Log.d(TAG, "getCallLogs: simNumber "+ simNumber+ " callerNumber "+callerNumber);
            /*    if(simNumber == (sim1)){
                    Log.d(TAG, "getCallLogs: sim 1");
                } else if(simNumber == (sim2)){
                    Log.d(TAG, "getCallLogs: sim 2");
                } else {
                    Log.d(TAG, "getCallLogs: sim not found");
                }
*/
                CallLogsModel model = new CallLogsModel();
                model.setCallerID(callerID);
                model.setCallerName(callerName);
                model.setCallerNumber(callerNumber);
                model.setCountryCode(callerCountry);
                model.setUserNumber(userNumber);
                model.setCallDateAndTime(time);
                model.setCallDuration(callDuration);
                model.setCallType(callType);

                callLogsList.add(model);

                if (callType == CallLog.Calls.INCOMING_TYPE) {
                    //incoming call
                } else if (callType == CallLog.Calls.OUTGOING_TYPE) {
                    //outgoing call
                } else if (callType == CallLog.Calls.MISSED_TYPE) {
                    //missed call
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

    private void requestReadCallLogsPermission() {
        ActivityCompat.requestPermissions(this, new
                String[]{READ_CALL_LOG}, CALL_LOG);
    }

    private void getSimSlotNumber() {

        if (Build.VERSION.SDK_INT > 22) {
            //for dual sim mobile
            SubscriptionManager localSubscriptionManager = SubscriptionManager.from(this);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            if (localSubscriptionManager.getActiveSubscriptionInfoCount() > 1) {
                //if there are two sims in dual sim mobile
                List localList = localSubscriptionManager.getActiveSubscriptionInfoList();
                SubscriptionInfo simInfo = (SubscriptionInfo) localList.get(0);
                SubscriptionInfo simInfo1 = (SubscriptionInfo) localList.get(1);

                sim1 = String.valueOf(simInfo.getSubscriptionId());
                sim2 = String.valueOf(simInfo1.getSubscriptionId());

            } else {
                //if there is 1 sim in dual sim mobile
                TelephonyManager tManager = (TelephonyManager) getBaseContext()
                        .getSystemService(Context.TELEPHONY_SERVICE);

                sim1 = tManager.getNetworkOperatorName();
            }
        }else{
            //below android version 22
            TelephonyManager tManager = (TelephonyManager) getBaseContext()
                    .getSystemService(Context.TELEPHONY_SERVICE);

            sim1 = tManager.getNetworkOperatorName();
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

}
