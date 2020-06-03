package com.naresh.hubpoc.defaultdialer;

import android.telecom.Call;
import android.telecom.InCallService;

import java.util.ArrayList;
import java.util.List;

public class CallService extends InCallService {

    List<Call> calls=new ArrayList<>();
    @Override
    public void onCallAdded(Call call) {
        super.onCallAdded(call);
        calls.add(call);
        new OngoingCall().setCall(call);
        CallActivity.start(this, call);
    }

    @Override
    public void onCallRemoved(Call call) {
        super.onCallRemoved(call);
        calls.remove(call);
        new OngoingCall().setCall(null);
    }
}
