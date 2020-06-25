package com.naresh.hubpoc.receiver;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.naresh.hubpoc.service.ForegroundService;

import java.util.Date;

public class CallReceiver extends PhoneCallReceiver {
    private static final String TAG = CallReceiver.class.getSimpleName();

    @Override
    protected void onIncomingCallStarted(Context ctx, String number, Date start) {
        super.onIncomingCallStarted(ctx, number, start);
        Toast.makeText(ctx, "onIncomingCallStarted "+number, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onIncomingCallStarted: ");
    }

    /*@Override
    protected void onIncomingCallReceived(Context ctx, String number, Date start)
    {
        //
    }*/

    @Override
    protected void onIncomingCallAnswered(Context ctx, String number, Date start)
    {
        super.onIncomingCallAnswered(ctx, number, start);
        Toast.makeText(ctx, "onIncomingCallAnswered "+number, Toast.LENGTH_SHORT).show();
        startService(ctx);
        Log.d(TAG, "onIncomingCallAnswered: ");
    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end)
    {
        Toast.makeText(ctx, "onIncomingCallEnded "+number, Toast.LENGTH_SHORT).show();

        Log.d(TAG, "onIncomingCallEnded: ");
    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start)
    {
        super.onOutgoingCallStarted(ctx, number, start);
        Toast.makeText(ctx, "onOutgoingCallStarted "+number, Toast.LENGTH_SHORT).show();

        startService(ctx);
        Log.d(TAG, "onOutgoingCallStarted: ");
    } 

    @Override 
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end)
    {
        Toast.makeText(ctx, "onOutgoingCallEnded "+number, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onOutgoingCallEnded: ");
    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start)
    {
        Log.d(TAG, "onMissedCall: ");
    }

    public void startService(Context context) {
        Intent serviceIntent = new Intent(context, ForegroundService.class);
        serviceIntent.putExtra("inputExtra", "Call Recording");
        ContextCompat.startForegroundService(context, serviceIntent);
    }

}
