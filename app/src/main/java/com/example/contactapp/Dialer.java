package com.example.contactapp;

import android.Manifest;
import android.content.*;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.telecom.*;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.example.contactapp.Modules.CallConnection;
import com.example.contactapp.Services.MyConnectionService;
import com.example.contactapp.Services.MyIncallService;
import com.example.contactapp.databinding.ActivityDialerBinding;

import java.util.List;
import java.util.UUID;


public class Dialer extends AppCompatActivity {

    private ActivityDialerBinding dialerBind;
    private Uri phoneNumberUri;
    private   CallConnection callConnection;
    private static final String PHONE_ACCOUNT_ID = "a547c15b-8655-4525-ba04-75f195831e1b";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialerBind = ActivityDialerBinding.inflate(getLayoutInflater());
        startService(new Intent(this, MyIncallService.class));
        startService(new Intent(this, MyConnectionService.class));
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
                String state = intent.getStringExtra("state");
                Log.d("status", "state");
                updateDialerUI(state);
            }
        }
    };

    private void updateDialerUI(String state) {
       dialerBind.statusCall.setText(state);
    }


    public void initiateCall(String phoneNumber) {
        Context context = this;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        TelecomManager telecomManager = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);
        unregisterAllPhoneAccounts(context, telecomManager);
        PhoneAccountHandle phoneAccountHandle = new PhoneAccountHandle(
                new ComponentName(context, MyConnectionService.class),
                PHONE_ACCOUNT_ID);

        if (telecomManager.getPhoneAccount(phoneAccountHandle) == null) {
            registerPhoneAccount(context, phoneAccountHandle);
            Log.d("Dialer", "initiateCall: Phone Account Registered, id:"+ PHONE_ACCOUNT_ID);
        }else {
            Log.d("Dialer", "initiateCall: Phone Account Not Registered");
        }

        Uri uri = Uri.fromParts(PhoneAccount.SCHEME_TEL, phoneNumber, null);
        Bundle extras = new Bundle();
        extras.putParcelable(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, phoneAccountHandle);
        telecomManager.placeCall(uri, extras);
    }
    private static void unregisterAllPhoneAccounts(Context context, TelecomManager telecomManager) {
        List<PhoneAccountHandle> phoneAccountHandles = telecomManager.getOwnSelfManagedPhoneAccounts();
        for (PhoneAccountHandle handle : phoneAccountHandles) {
            telecomManager.unregisterPhoneAccount(handle);
        }
    }
    private static void registerPhoneAccount(Context context, PhoneAccountHandle phoneAccountHandle) {
        PhoneAccount phoneAccount = PhoneAccount.builder(phoneAccountHandle, "My_App_Phone_Account")
                .setCapabilities(PhoneAccount.CAPABILITY_SELF_MANAGED)
                .build();

        TelecomManager telecomManager = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);
        telecomManager.registerPhoneAccount(phoneAccount);
    }
    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(callStatusReceiver, new IntentFilter("CALL_STATUS_UPDATE"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(callStatusReceiver);
    }
}
