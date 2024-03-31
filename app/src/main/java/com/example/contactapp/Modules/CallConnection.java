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


    public void initialize(Uri address, String callerDisplayName) {
        setAddress(address, TelecomManager.PRESENTATION_ALLOWED);
        setCallerDisplayName(callerDisplayName, TelecomManager.PRESENTATION_ALLOWED);
        setConnectionProperties(Connection.PROPERTY_SELF_MANAGED);
        setConnectionCapabilities(Connection.CAPABILITY_SUPPORT_HOLD | Connection.CAPABILITY_HOLD);
        setAudioModeIsVoip(true);
    }

    @Override
    public void onDisconnect() {
        super.onDisconnect();
        Log.d(TAG, "onDisconnected: " );
    }

    @Override
    public void onReject(int rejectReason) {
        super.onReject(rejectReason);
        Log.d(TAG, "onReject: "+ rejectReason);
    }



    @Override
    public void onAnswer() {
        super.onAnswer();
        Log.d(TAG, "onAnswer: Call answered");
    }

    @Override
    public void onReject() {
        super.onReject();
        Log.d(TAG, "onReject: Call rejected");
    }


}
