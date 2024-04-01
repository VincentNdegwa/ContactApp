package com.example.contactapp.Services;
import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.telecom.*;
import android.telecom.InCallService;
import android.util.Log;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.example.contactapp.Dialer;
import com.example.contactapp.Interfaces.CallConstants;


public class MyIncallService extends InCallService  {

    private TelecomManager mTelecomManager;
    private AudioManager audioManager;

    private LocalBroadcastManager localBroadcastManager;
    private  Call mCall;
    @Override
    public void onCreate() {
        super.onCreate();
        mTelecomManager = (TelecomManager)getSystemService(TELECOM_SERVICE);
        audioManager =(AudioManager)getSystemService(Context.AUDIO_SERVICE);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        registerMyReceivers();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("USER_CALL_STATE")){
                String state = intent.getStringExtra("state");
                switch (state) {
                    case CallConstants.REJECT_CALL:
                        rejectCall();
                        break;
                    case CallConstants.ON_HOLD_CALL:
                        holdCall();
                        break;
                    case CallConstants.SPEAKER_ON:
                        speakerOn();
                        break;
                    case CallConstants.SPEAKER_OFF:
                        speakerOff();
                        break;
                    case CallConstants.ADD_CALL:
                        addCall();
                        break;
                    case CallConstants.REMOVE_HOLD:
                        removeHold();
                        break;
                    case CallConstants.START_RECORDING:
                        startRecording();
                        break;
                    case CallConstants.STOP_RECORDING:
                        stopRecording();
                        break;
                    case CallConstants.ANSWER_CALL:
                        answerCall();
                        break;
                    case CallConstants.MUTE_CALL:
                        muteCall();
                        break;
                    case CallConstants.UNMUTE_CALL:
                        unmuteCall();
                        break;
                    default:
                        // Handle unknown state if necessary
                        break;
                }
            }
        }
    };



    private void registerMyReceivers() {
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("USER_CALL_STATE"));
    }

    @Override
    public void onCallAdded(Call call) {
        super.onCallAdded(call);
        mCall =call;
        call.registerCallback(new Call.Callback() {
            @Override
            public void onStateChanged(Call call, int state) {
                super.onStateChanged(call, state);
                Log.e("Call State Changed", "onStateChanged: call state changed to " + state);
                String stateString = getStateString(state);
                broadcastCallState(stateString);
            }
        });

        Dialer.start(mCall, this);
    }

    private String getStateString(int state) {
        switch (state) {
            case Call.STATE_NEW:
                return "NEW";
            case Call.STATE_DIALING:
                return "DIALING";
            case Call.STATE_RINGING:
                return "RINGING";
            case Call.STATE_ACTIVE:
                return "ACTIVE";
            case Call.STATE_HOLDING:
                return "HOLDING";
            case Call.STATE_DISCONNECTED:
                return "DISCONNECTED";
            case Call.STATE_DISCONNECTING:
                return "DISCONNECTING";
            default:
                return "UNKNOWN";
        }
    }

    private void rejectCall() {
        if (mCall != null) {
            mCall.disconnect();
        }
    }
    private void holdCall() {
        if (mCall != null) {
            mCall.hold();
        }
    }
    private void unmuteCall() {
    }

    private void answerCall() {
    }

    private void stopRecording() {
    }

    private void startRecording() {
    }

    private void removeHold() {
        if (mCall != null){
            mCall.unhold();
        }
    }

    private void addCall() {
    }

    private void speakerOff() {
        if (audioManager != null && checkCallingOrSelfPermission(Manifest.permission.MODIFY_AUDIO_SETTINGS) == PackageManager.PERMISSION_GRANTED) {
            audioManager.setSpeakerphoneOn(false);
        }
    }

    private void speakerOn() {
        if (audioManager != null && checkCallingOrSelfPermission(Manifest.permission.MODIFY_AUDIO_SETTINGS) == PackageManager.PERMISSION_GRANTED) {
            audioManager.setSpeakerphoneOn(true);
        }
    }

    public void muteCall() {
        if (audioManager != null && checkCallingOrSelfPermission(Manifest.permission.MODIFY_AUDIO_SETTINGS) == PackageManager.PERMISSION_GRANTED) {
            audioManager.setMicrophoneMute(true);
        }
    }



    private void broadcastCallState(String stateString) {
        Intent intent = new Intent("CALL_STATUS_UPDATE");
        intent.putExtra("state", stateString);
        localBroadcastManager.sendBroadcast(intent);
    }

}

