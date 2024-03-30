package com.example.contactapp.Services;
import android.content.Intent;
import android.os.Bundle;
import android.telecom.*;
import android.telecom.InCallService;
import android.telephony.TelephonyManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


public class Connecting extends InCallService {
    private LocalBroadcastManager localBroadcastManager;
    @Override
    public void onCreate() {
        super.onCreate();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void onConnectionEvent(Call call, String event, Bundle extras) {
        int state = call.getState();
        if (event.equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)){
            state = call.getState();
        }
        updateDialerUI(state);
    }

    private void updateDialerUI(int state){
        Intent intent = new Intent("CALL_STATUS_UPDATE");
        intent.putExtra("state", state);
        localBroadcastManager.sendBroadcast(intent);
    }
}

