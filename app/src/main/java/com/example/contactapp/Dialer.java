package com.example.contactapp;

import android.content.*;
import android.net.Uri;
import android.telecom.*;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.example.contactapp.Interfaces.CallConstants;
import com.example.contactapp.Modules.CallConnection;
import com.example.contactapp.Services.MyConnectionService;
import com.example.contactapp.Services.MyIncallService;
import com.example.contactapp.databinding.ActivityDialerBinding;

import java.util.Timer;
import java.util.TimerTask;


public class Dialer extends AppCompatActivity {

    private ActivityDialerBinding dialerBind;
    private Uri phoneNumberUri;
    private   CallConnection callConnection;
    private LocalBroadcastManager localBroadcastManager;
    private Long startCallTimeMills = 0L;
    private Long elapsedTimeMills;
    private static Call callInst;
    private Boolean SPEAKER = false;
    private  Boolean MUTE = false;
    private Boolean RECORDING = false;
    private Boolean ONHOLD = false;
    private Boolean DIALPAD = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialerBind = ActivityDialerBinding.inflate(getLayoutInflater());
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        phoneNumberUri = getIntent().getData();
        setContentView(dialerBind.getRoot());
        if (callInst.getState() == Call.STATE_ACTIVE){
            startTimer();
        }
        eventListeners();
    }



    private void eventListeners() {
        dialerBind.declineCallButton.setOnClickListener(view -> {
            updateCallStatus(CallConstants.REJECT_CALL);
        });
        dialerBind.recordButton.setOnClickListener(view -> {
            RECORDING = !RECORDING;
            updateOnCallInterface(dialerBind.recordButton, RECORDING);
            updateCallStatus(RECORDING ? CallConstants.START_RECORDING : CallConstants.STOP_RECORDING);
        });
        dialerBind.dialButton.setOnClickListener(view -> {
            // Implement dial functionality here
        });
        dialerBind.addCallButton.setOnClickListener(view -> {
            // Implement add call functionality here
        });
        dialerBind.muteButton.setOnClickListener(view -> {
            MUTE = !MUTE;
            updateOnCallInterface(dialerBind.muteButton, MUTE);
            updateCallStatus(MUTE ? CallConstants.MUTE_CALL : CallConstants.UNMUTE_CALL);
        });
        dialerBind.voiceButton.setOnClickListener(view -> {
            SPEAKER = !SPEAKER;
            updateOnCallInterface(dialerBind.voiceButton, SPEAKER);
            updateCallStatus(SPEAKER ? CallConstants.SPEAKER_ON : CallConstants.SPEAKER_OFF);
        });
        dialerBind.holdButton.setOnClickListener(view -> {
            ONHOLD = !ONHOLD;
            updateOnCallInterface(dialerBind.holdButton, ONHOLD);
            updateCallStatus(ONHOLD ? CallConstants.ON_HOLD_CALL : CallConstants.REMOVE_HOLD);
        });
    }

    private void updateOnCallInterface(ImageButton button,boolean b) {
        if (b){
            button.setBackground(ContextCompat.getDrawable(this,R.drawable.button_bg_white_round));
        }else {
            button.setBackground(ContextCompat.getDrawable(this,R.drawable.button_bg_saturated_round));
        }
    }

    private void updateOnCallInterface(ImageButton button) {
    }

    private void updateOnCallInterface( boolean b) {
    }

    private void updateOnCallInterface() {
    }

    private void updateCallStatus(String state) {
        Intent intent = new Intent("USER_CALL_STATE");
        intent.putExtra("state", state);
        localBroadcastManager.sendBroadcast(intent);
    }

    private BroadcastReceiver callStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("CALL_STATUS_UPDATE")){
                String state = intent.getStringExtra("state");
                if (state == "ACTIVE" ){
                    startTimer();
                    startCallTimeMills = System.currentTimeMillis();
                }else if (state == "DISCONNECTED"){
                    stopTimer();
                    elapsedTimeMills = System.currentTimeMillis() - startCallTimeMills;
                    formatCallTime(elapsedTimeMills);
                    closeInterface();
                }
                updateDialerUI(state);
            }
        }
    };

    private void closeInterface() {
        super.onBackPressed();
    }

    private Timer timer;
    private void startTimer() {
        if (timer == null) {
            long startCallTimeMillis = System.currentTimeMillis();
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    long elapsedTimeMillis = System.currentTimeMillis() - startCallTimeMillis;
                    runOnUiThread(() -> {
                        String timeString = formatCallTime(elapsedTimeMillis);
                        dialerBind.statusCall.setText(timeString);
                    });
                }
            }, 0, 1000);
        }
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }


    private String formatCallTime(Long elapsedTimeMills) {
        long seconds = elapsedTimeMills / 1000 % 60;
        long minutes = elapsedTimeMills / (1000 * 60) % 60;
        long hours = elapsedTimeMills / (1000 * 60 * 60) % 24;
        long days = elapsedTimeMills / (1000 * 60 * 60 * 24);

        String timeString = "";
        if (days > 0) {
            timeString += days + " days ";
        }
        if (hours > 0) {
            timeString += String.format("%02d:", hours);
        }
        timeString += String.format("%02d:", minutes);
        timeString += String.format("%02d", seconds);
        return timeString.trim();
    }


    private void updateDialerUI(String state) {
       dialerBind.statusCall.setText(state);
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

    public static void start(Call mCall, Context context){
        callInst = mCall;
        Intent intent = new Intent(context, Dialer.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(mCall.getDetails().getHandle());
        context.startActivity(intent);
    }
}
