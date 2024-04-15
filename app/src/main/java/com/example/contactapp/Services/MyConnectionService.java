package com.example.contactapp.Services;

import android.telecom.*;
import android.text.TextUtils;
import android.util.Log;
import com.example.contactapp.Modules.CallConnection;
import com.google.gson.Gson;

import java.util.List;

public class MyConnectionService extends ConnectionService {

    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public void onCreateOutgoingConnectionFailed(PhoneAccountHandle connectionManagerPhoneAccount, ConnectionRequest request) {
        super.onCreateOutgoingConnectionFailed(connectionManagerPhoneAccount, request);
    }
    @Override
    public Connection onCreateOutgoingConnection(PhoneAccountHandle connectionManagerPhoneAccount, ConnectionRequest request) {

        Log.e("MyConnectionService", "ConnectionRequest: "+ request.getAddress() );
        Log.e("MyConnectionService", "PhoneAccountHandle: "+ connectionManagerPhoneAccount );

        CallConnection connection = new CallConnection();
        String name = TextUtils.isEmpty(request.getAddress().toString())?request.getAddress().toString(): "Unknown";
        connection.setAddress(request.getAddress(), TelecomManager.PRESENTATION_ALLOWED);
        connection.setCallerDisplayName(name, TelecomManager.PRESENTATION_ALLOWED);
        connection.setConnectionProperties(Connection.PROPERTY_SELF_MANAGED);
        connection.setConnectionCapabilities(Connection.CAPABILITY_SUPPORT_HOLD | Connection.CAPABILITY_HOLD);
        connection.setAudioModeIsVoip(true);
        return connection;
    }
}
