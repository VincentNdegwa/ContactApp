package com.example.contactapp.Modules;

import android.telecom.CallAudioState;
import android.telecom.Connection;

public class CallConnection extends Connection {
    @Override
    public void onCallAudioStateChanged(CallAudioState state) {
        super.onCallAudioStateChanged(state);
    }

    @Override
    public void onDisconnect() {
        super.onDisconnect();
    }

    @Override
    public void onAbort() {
        super.onAbort();
    }

    @Override
    public void onHold() {
        super.onHold();
    }

    @Override
    public void onUnhold() {
        super.onUnhold();
    }

    @Override
    public void onAnswer() {
        super.onAnswer();
    }

    @Override
    public void onReject() {
        super.onReject();
    }

    @Override
    public void onShowIncomingCallUi() {
        super.onShowIncomingCallUi();
    }
}
