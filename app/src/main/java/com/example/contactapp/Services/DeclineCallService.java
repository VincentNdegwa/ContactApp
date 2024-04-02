package com.example.contactapp.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class DeclineCallService extends Service {
    private LocalBroadcastManager localBroadcastManager;
    private String TAG = "DeclineCallService";
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        localBroadcastManager  = LocalBroadcastManager.getInstance(this);
        String state = intent.getStringExtra("state");
        updateCall(state);
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateCall(String state) {
        Intent intent = new Intent("USER_CALL_STATE");
        intent.putExtra("state", state);
        System.out.println("sending"+state);
        localBroadcastManager.sendBroadcast(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
