package com.naresh.hubpoc.defaultdialer;

import android.telecom.Call;
import android.telecom.VideoProfile;



import androidx.annotation.Nullable;
import io.reactivex.subjects.BehaviorSubject;

public class OngoingCall {

    public static BehaviorSubject<Integer> state = BehaviorSubject.create();
    private static Call call;

    private Object callback = new Call.Callback() {
        @Override
        public void onStateChanged(Call call, int newState) {
            super.onStateChanged(call, newState);
            state.onNext(newState);
        }
    };

    public final void setCall(@Nullable Call value) {
        if (call != null) {
            call.unregisterCallback((Call.Callback)callback);
        }

        if (value != null) {
            value.registerCallback((Call.Callback)callback);
            state.onNext(value.getState());
        }

        call = value;
    }

    public void answer() {
        assert call != null;
        call.answer(VideoProfile.STATE_AUDIO_ONLY);
    }

    public void hangup() {
        assert call != null;
        call.disconnect();
    }
    public void hold() {
        assert call != null;
        call.hold();
    }

    public void unHold() {
        assert call != null;
        call.unhold();
    }
    public void conference(@Nullable Call value){
        assert call != null;
        call.conference(value);
    }
}
