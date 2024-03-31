package com.example.contactapp.Services;

import android.telecom.*;
import android.util.Log;
import com.example.contactapp.Modules.CallConnection;

import java.util.List;

public class MyConnectionService extends ConnectionService {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MyConnectionService", "onCreate ");
        //        List<PhoneAccountHandle> phoneAccountHandleList = telecomManager.getCallCapablePhoneAccounts();

//        PhoneAccountHandle phoneAccountHandle = phoneAccountHandleList.get(0);
    }

    @Override
    public void onCreateOutgoingConnectionFailed(PhoneAccountHandle connectionManagerPhoneAccount, ConnectionRequest request) {
        Log.d("MyConnectionService", "onCreateOutgoingConnectionFailed: "+ request.describeContents());
        super.onCreateOutgoingConnectionFailed(connectionManagerPhoneAccount, request);
    }

    @Override
    public Connection onCreateOutgoingConnection(PhoneAccountHandle connectionManagerPhoneAccount, ConnectionRequest request) {

        Log.e("MyConnectionService", "ConnectionRequest: "+ request.getAddress() );
        Log.e("MyConnectionService", "PhoneAccountHandle: "+ connectionManagerPhoneAccount );

        CallConnection connection = new CallConnection();

        connection.initialize(request.getAddress(), "Caller");
        return connection;
    }
}
