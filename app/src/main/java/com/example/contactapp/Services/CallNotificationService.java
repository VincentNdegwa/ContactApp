package com.example.contactapp.Services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Person;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.telecom.Call;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.example.contactapp.R;
import com.example.contactapp.Interfaces.CallConstants;

public class CallNotificationService extends Service {
    private String TAG = "CallNotificationService";
    private static final int NOTIFICATION_ID= 10;
    private static final String INCOMING_CHANNEL_ID = "INCOMING_CHANNEL_ID";
    private LocalBroadcastManager localBroadcastManager;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        registerBroadCastReceiver();
        String callerName = intent.getStringExtra("callerName");
        PendingIntent declinePendingIntent = intent.getParcelableExtra("declinePendingIntent");
        PendingIntent answerPendingIntent = intent.getParcelableExtra("answerPendingIntent");
        PendingIntent contentIntent = intent.getParcelableExtra("contentIntent");

        Notification notification = buildCallNotification(callerName, declinePendingIntent, answerPendingIntent, contentIntent);
       notification.flags = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;
        startForeground(NOTIFICATION_ID, notification);

        return START_STICKY;
    }



    private Notification buildCallNotification(String callerName, PendingIntent declinePendingIntent, PendingIntent answerPendingIntent, PendingIntent contentIntent) {
        Notification.Builder builder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            builder = new Notification.Builder(this, INCOMING_CHANNEL_ID)
                    .setContentTitle("Incoming call")
                    .setContentText(callerName)
                    .setContentIntent(contentIntent)
                    .setSmallIcon(R.drawable.phone_icon)
                    .setCategory(Notification.CATEGORY_CALL)
                    .setAutoCancel(false)
                    .setStyle(Notification.CallStyle.forIncomingCall(
                            new Person.Builder().setName("callerName").build(),
                            declinePendingIntent,
                            answerPendingIntent
                    ));

        }

        if (builder == null) {
            builder = new Notification.Builder(this, INCOMING_CHANNEL_ID)
                    .setContentTitle("Incoming call")
                    .setContentText("callerName")
                    .setPriority(Notification.PRIORITY_MAX)
                    .setOngoing(true)
                    .setSmallIcon(R.drawable.phone_icon)
                    .setCategory(Notification.CATEGORY_CALL);
        }

        return builder.build();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int state = intent.getIntExtra("state", -1);
            switch (state){
                case Call.STATE_ACTIVE:
                    stopCalRingTone();
                    Log.e(TAG, "onReceive: "+ "ANSWER CALL" );
                    break;
                case Call.STATE_DISCONNECTED:
                    stopCalRingTone();
                    stopForeground(true);
                    Log.e(TAG, "onReceive: "+ "REJECT CALL" );
                    break;
                default:
                    Log.e(TAG, "onReceive: "+ "DEFAULT" );
            }

        }
    };

    private void stopCalRingTone() {
        System.out.println("trying to stop ringtone");
        Ringtone ringtone = RingtoneManager.getRingtone(this, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
        ringtone.stop();
    }

    private void registerBroadCastReceiver() {
        localBroadcastManager.registerReceiver(broadcastReceiver, new IntentFilter("SERVICE_NOTIFICATION_STATE"));
    }
}
