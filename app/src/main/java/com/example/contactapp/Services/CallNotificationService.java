package com.example.contactapp.Services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Person;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.IBinder;
import android.telecom.Call;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.example.contactapp.R;

public class CallNotificationService extends Service {
    private String TAG = "CallNotificationService";
    private static final int NOTIFICATION_ID = 10;
    private static final String INCOMING_CHANNEL_ID = "INCOMING_CHANNEL_ID";
    private LocalBroadcastManager localBroadcastManager;
    private static Call mcall;
    private Notification.Builder ongoingNotificationBuilder;

    private boolean ongoingNotificationCreated = false;
    private static PendingIntent declinePendingIntent;
    private static PendingIntent answerPendingIntent;
    private static PendingIntent contentIntent;
    private static PendingIntent pendingFullScreenIntent;
    private static String callerName;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        registerBroadCastReceiver();
        callerName = intent.getStringExtra("callerName");
        declinePendingIntent = intent.getParcelableExtra("declinePendingIntent");
        answerPendingIntent = intent.getParcelableExtra("answerPendingIntent");
        contentIntent = intent.getParcelableExtra("contentIntent");
        pendingFullScreenIntent = intent.getParcelableExtra("pendingFullScreenIntent");

        if (answerPendingIntent == null && pendingFullScreenIntent == null) {
            if (!ongoingNotificationCreated) {
                ongoingNotificationBuilder = buildOngoingNotificationBuilder(callerName, declinePendingIntent, contentIntent);
                Notification notification = ongoingNotificationBuilder.build();
                notification.flags = Notification.FLAG_ONGOING_EVENT;
                notification.visibility =Notification.VISIBILITY_PRIVATE;
                notification.priority =Notification.PRIORITY_LOW;
                startForeground(NOTIFICATION_ID, notification);
                ongoingNotificationCreated = true;
            }
        } else {
            Notification notification = buildCallNotification(callerName, declinePendingIntent, answerPendingIntent, contentIntent, pendingFullScreenIntent);
            notification.flags = Notification.FLAG_ONGOING_EVENT;
            startForeground(NOTIFICATION_ID, notification);
        }
        return START_STICKY;
    }

    private Notification.Builder buildOngoingNotificationBuilder(String callerName, PendingIntent declinePendingIntent, PendingIntent contentIntent) {
        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            builder = new Notification.Builder(this, INCOMING_CHANNEL_ID)
                    .setContentTitle("Outgoing call")
                    .setContentText(callerName)
                    .setContentIntent(contentIntent)
                    .setSmallIcon(R.drawable.phone_icon)
                    .setVisibility(Notification.VISIBILITY_PRIVATE)
                    .setStyle(Notification.CallStyle.forOngoingCall(
                            new Person.Builder().setName("callerName").build(),
                            declinePendingIntent

                    ));
        } else {
            builder = new Notification.Builder(this, INCOMING_CHANNEL_ID)
                    .setContentTitle("Outgoing call")
                    .setContentText("callerName")
                    .setPriority(Notification.PRIORITY_LOW)
                    .setOngoing(true)
                    .setSmallIcon(R.drawable.phone_icon)
                    .setCategory(Notification.CATEGORY_CALL);
        }
        return builder;
    }

    private Notification buildCallNotification(String callerName, PendingIntent declinePendingIntent, PendingIntent answerPendingIntent, PendingIntent contentIntent, PendingIntent pendingFullScreenIntent) {
        Notification.Builder builder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            builder = new Notification.Builder(this, INCOMING_CHANNEL_ID)
                    .setContentTitle("Incoming call")
                    .setContentText(callerName)
                    .setContentIntent(contentIntent)
                    .setSmallIcon(R.drawable.phone_icon)
                    .setCategory(Notification.CATEGORY_CALL)
                    .setAutoCancel(false)
                    .setOngoing(true)
                    .setFullScreenIntent(pendingFullScreenIntent, true)
                    .setFlag(Notification.FLAG_ONGOING_EVENT, true)
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
            switch (state) {
                case Call.STATE_ACTIVE:
                    if (!ongoingNotificationCreated) {
                        ongoingNotificationBuilder = buildOngoingNotificationBuilder(callerName,declinePendingIntent , contentIntent);
                        Notification notification = ongoingNotificationBuilder.build();
                        notification.flags = Notification.FLAG_ONGOING_EVENT;
                        startForeground(NOTIFICATION_ID, notification);
                        ongoingNotificationCreated = true;
                    }
                    break;
                case Call.STATE_DISCONNECTED:
                    stopForeground(true);
                    break;
                default:
                    Log.e(TAG, "onReceive: " + "DEFAULT");
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
