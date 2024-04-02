package com.example.contactapp;

import android.content.Context;
import android.content.Intent;
import android.telecom.Call;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.example.contactapp.Interfaces.CallConstants;
import com.example.contactapp.databinding.ActivityIncommingCallBinding;

public class IncommingCall extends AppCompatActivity {

    private ActivityIncommingCallBinding binding;
    LocalBroadcastManager localBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        binding = ActivityIncommingCallBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        eventListeners();
    }

    private void eventListeners() {
        binding.acceptCall.setOnClickListener(view -> {
            SendBroadCast(CallConstants.ANSWER_CALL);
        });
        binding.declineCall.setOnClickListener(view -> {
            SendBroadCast(CallConstants.REJECT_CALL);
        });
    }

    private void SendBroadCast(String state) {
        Intent intent = new Intent("USER_CALL_STATE");
        intent.putExtra("state", state);
        localBroadcastManager.sendBroadcast(intent);
    }

    @Override
    protected void onPause() {
        super.onBackPressed();
        super.onPause();
    }

    public static void start(Call mCall, Context context) {
        Intent  intent = new Intent(context, IncommingCall.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}