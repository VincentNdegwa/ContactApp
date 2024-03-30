package com.example.contactapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.telecom.Call;
import android.telecom.Connection;
import android.telecom.TelecomManager;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import com.example.contactapp.Modules.CallConnection;
import com.example.contactapp.Modules.OngoingCall;
import com.example.contactapp.Services.Connecting;
import com.example.contactapp.databinding.ActivityDialerBinding;

public class Dialer extends AppCompatActivity {

    private ActivityDialerBinding dialerBind;

    private Uri phoneNumberUri;
    private   CallConnection callConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialerBind = ActivityDialerBinding.inflate(getLayoutInflater());
        setContentView(dialerBind.getRoot());

        phoneNumberUri = getIntent().getData();

        if (phoneNumberUri != null) {
            String phoneNumber = phoneNumberUri.getSchemeSpecificPart();
            initiateCall(phoneNumber);
        }
        callConnection = new CallConnection();
        System.out.println(callConnection);
        OngoingCall.addCallStateListener(new OngoingCall.CallStateListener() {
            @Override
            public void onCallStateChanged(int newState) {
                updateCallStatus(newState);
            }
        });
    }
//
//    private void initiateCall(String phoneNumber) {
//        Uri uri = Uri.fromParts("tel", phoneNumber, null);
//        TelecomManager telecomManager = (TelecomManager) getSystemService(TELECOM_SERVICE);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        telecomManager.placeCall(uri, null);
//        Log.d("call", "initiateCall: "+callConnection.STATE_DIALING);
//        Call.Callback callback = new Call.Callback() {
//            @Override
//            public void onStateChanged(Call call, int state) {
//                super.onStateChanged(call, state);
//                Log.d("call", "Call state changed: " + state);
//                updateCallStatus(state);
//            }
//        };
//
//    }
private void initiateCall(String phoneNumber) {
    Uri uri = Uri.fromParts("tel", phoneNumber, null);
    Intent callIntent = new Intent(Intent.ACTION_CALL, uri);
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
        return;
    }
    Log.d("call", "initiateCall: initiating");
    Intent intent = new Intent(this,Connecting.class);
    intent.setData(Uri.parse("tel:" + phoneNumber));
    startService(intent);

}


    private void updateCallStatus(int state) {
        String statusText;
        switch (state) {
            case Call.STATE_NEW:
                statusText = "New call";
                break;
            case Call.STATE_DIALING:
                statusText = "Dialing...";
                break;
            case Call.STATE_RINGING:
                statusText = "Ringing";
                break;
            case Call.STATE_HOLDING:
                statusText = "Call on hold";
                break;
            case Call.STATE_ACTIVE:
                statusText = "Call connected";
                break;
            case Call.STATE_DISCONNECTED:
                statusText = "Call disconnected";
                break;
            default:
                statusText = "Unknown";
        }
        dialerBind.statusCall.setText(statusText);
    }



    public static void start(Context context, Call call) {
        Intent intent = new Intent(context, Dialer.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(call.getDetails().getHandle());
        context.startActivity(intent);
    }
}
