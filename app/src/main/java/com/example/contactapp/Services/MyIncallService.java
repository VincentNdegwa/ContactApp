package com.example.contactapp.Services;
import android.content.Intent;
import android.os.Bundle;
import android.telecom.*;
import android.telecom.InCallService;
import android.util.Log;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


public class MyIncallService extends InCallService {
    private LocalBroadcastManager localBroadcastManager;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MyIncallService", "onCreate");
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void onCallAdded(Call call) {
        int state = call.getState();
        Log.e("Call Added", "onCallAdded: call added " +state);
        String stateString = getStateString(state);
        broadcastCallState(stateString);
        super.onCallAdded(call);
    }

    @Override
    public void onConnectionEvent(Call call, String event, Bundle extras) {
        Log.e("Connecting", "onConnectionEvent: connection changed");
        int state = call.getState();
        String stateString = getStateString(state);
        broadcastCallState(stateString);
    }

    private String getStateString(int state) {
        switch (state) {
            case Call.STATE_ACTIVE:
                return "Active";
            case Call.STATE_AUDIO_PROCESSING:
                return "Audio Processing";
            case Call.STATE_CONNECTING:
                return "Connecting";
            case Call.STATE_DIALING:
                return "Dialing";
            case Call.STATE_DISCONNECTED:
                return "Disconnected";
            case Call.STATE_DISCONNECTING:
                return "Disconnecting";
            case Call.STATE_HOLDING:
                return "Holding";
            case Call.STATE_NEW:
                return "New";
            case Call.STATE_PULLING_CALL:
                return "Pulling Call";
            case Call.STATE_RINGING:
                return "Ringing";
            default:
                return "Unknown";
        }
    }

    private void broadcastCallState(String stateString) {
        Intent intent = new Intent("CALL_STATUS_UPDATE");
        intent.putExtra("state", stateString);
        localBroadcastManager.sendBroadcast(intent);
    }

}

