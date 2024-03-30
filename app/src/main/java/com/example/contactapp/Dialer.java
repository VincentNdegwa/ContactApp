package com.example.contactapp;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.telecom.TelecomManager;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import com.example.contactapp.Modules.CallConnection;
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
        startService(new Intent(this, Connecting.class));
        setContentView(dialerBind.getRoot());

        phoneNumberUri = getIntent().getData();

        if (phoneNumberUri != null) {
            String phoneNumber = phoneNumberUri.getSchemeSpecificPart();
            initiateCall(phoneNumber);
        }
        callConnection = new CallConnection();
    }

    private BroadcastReceiver callStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("CALL_STATUS_UPDATE")){
                int state = intent.getIntExtra("state", -1);
                updateDialerUI(state);
            }
        }
    }

    private void updateDialerUI(int state) {

    }


    private void initiateCall(String phoneNumber) {
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
        return;
    }
    Log.d("call", "initiateCall: initiating");
    TelecomManager telecomManager = (TelecomManager) getSystemService(Context.TELECOM_SERVICE);
    telecomManager.placeCall(Uri.parse("tel:" + phoneNumber), new Bundle());
}
}
