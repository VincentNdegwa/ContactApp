package com.example.contactapp.Services;

import android.net.Uri;
import android.telecom.*;
import android.util.Log;
import com.example.contactapp.Modules.CallConnection;

public class Connecting extends ConnectionService {
    @Override
    public Connection onCreateOutgoingConnection(PhoneAccountHandle connectionManagerPhoneAccount, ConnectionRequest request) {
        Log.d("data", "onCreateOutgoingConnection: "+ request);
       String phone = request.getAddress().getSchemeSpecificPart();
        CallConnection connection = new CallConnection();
        connection.setAddress(Uri.parse(phone), TelecomManager.PRESENTATION_ALLOWED);
        connection.setInitializing();
        connection.setInitializing();
        connection.setDialing();
        return connection;
    }

}
