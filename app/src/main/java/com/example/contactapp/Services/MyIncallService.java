package com.example.contactapp.Services;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.telecom.*;
import android.telecom.InCallService;
import android.util.Log;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


public class MyIncallService extends InCallService {
    private TelecomManager mTelecomManager;
    private AudioManager audioManager;

    private LocalBroadcastManager localBroadcastManager;
    private  Call mCall;
    @Override
    public void onCreate() {
        super.onCreate();
        mTelecomManager = (TelecomManager)getSystemService(TELECOM_SERVICE);
        audioManager =(AudioManager)getSystemService(Context.AUDIO_SERVICE);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void onCallAdded(Call call) {
        super.onCallAdded(call);
        mCall =call;
        int initialState = call.getState();
        Log.e("Call Added", "onCallAdded: call added " + initialState);

        call.registerCallback(new Call.Callback() {
            @Override
            public void onStateChanged(Call call, int state) {
                super.onStateChanged(call, state);
                Log.e("Call State Changed", "onStateChanged: call state changed to " + state);
                String stateString = getStateString(state);
                broadcastCallState(stateString);
            }
        });
    }

    private String getStateString(int state) {
        switch (state) {
            case Call.STATE_NEW:
                return "NEW";
            case Call.STATE_DIALING:
                return "DIALING";
            case Call.STATE_RINGING:
                return "RINGING";
            case Call.STATE_ACTIVE:
                return "ACTIVE";
            case Call.STATE_HOLDING:
                return "HOLDING";
            case Call.STATE_DISCONNECTED:
                return "DISCONNECTED";
            case Call.STATE_DISCONNECTING:
                return "DISCONNECTING";
            default:
                return "UNKNOWN";
        }
    }


    private void holdCall() {
        if (mCall != null) {
            mCall.hold();
            mCall.hold();
        }
    }

    public void muteCall() {
        if (audioManager != null && checkCallingOrSelfPermission(Manifest.permission.MODIFY_AUDIO_SETTINGS) == PackageManager.PERMISSION_GRANTED) {
            audioManager.setMicrophoneMute(true);
        }
    }

    private void rejectCall() {
        if (mCall != null) {
            mCall.reject(false, null);
        }
    }

    private void broadcastCallState(String stateString) {
        Intent intent = new Intent("CALL_STATUS_UPDATE");
        intent.putExtra("state", stateString);
        localBroadcastManager.sendBroadcast(intent);
    }

}

