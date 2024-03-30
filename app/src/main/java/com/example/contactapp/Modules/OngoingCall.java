package com.example.contactapp.Modules;

import android.telecom.Call;
import android.telecom.VideoProfile;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class OngoingCall {
    private static List<CallStateListener> listeners = new ArrayList<>();
    private static Call call;

    public interface CallStateListener {
        void onCallStateChanged(int newState);
    }

    public static void addCallStateListener(CallStateListener listener) {
        listeners.add(listener);
    }

    public static void removeCallStateListener(CallStateListener listener) {
        listeners.remove(listener);
    }

    private static void notifyCallStateChanged(int newState) {
        for (CallStateListener listener : listeners) {
            listener.onCallStateChanged(newState);
        }
    }

    public void setCall(@Nullable Call value) {
        if (call != null) {
            call.unregisterCallback(callback);
        }

        if (value != null) {
            value.registerCallback(callback);
            notifyCallStateChanged(value.getState());
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

    private Call.Callback callback = new Call.Callback() {
        @Override
        public void onStateChanged(Call call, int newState) {
            super.onStateChanged(call, newState);
            notifyCallStateChanged(newState);
        }
    };
}
