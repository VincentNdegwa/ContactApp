package com.example.contactapp.Modules;

import android.net.Uri;
import android.telecom.Connection;
import android.telecom.DisconnectCause;
import android.telecom.TelecomManager;
import android.util.Log;

public class CallConnection extends Connection {

    private static final String TAG = "CallConnection";

    @Override
    public void onStateChanged(int state) {
        super.onStateChanged(state);
        Log.d(TAG, "onStateChanged: " + stateToString(state));
    }




}
