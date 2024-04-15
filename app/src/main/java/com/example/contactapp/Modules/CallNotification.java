package com.example.contactapp.Modules;


import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Person;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.telecom.Call;
import android.text.TextUtils;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationChannelCompat;
import androidx.core.app.NotificationManagerCompat;
import com.example.contactapp.Dialer;
import com.example.contactapp.IncommingCall;
import com.example.contactapp.Interfaces.CallConstants;
import com.example.contactapp.R;
import com.example.contactapp.Services.AnswerCallService;
import com.example.contactapp.Services.CallNotificationService;
import com.example.contactapp.Services.DeclineCallService;
import com.example.contactapp.Services.MyIncallService;


public class CallNotification {
    private static final String INCOMING_CHANNEL_ID = "INCOMING_CHANNEL_ID";
    private static final String INCOMING_NOTIFICATION_TAG = "INCOMING_NOTIFICATION_TAG";

    public static void showIncomingCallNotification(Call call, Context context) {
        NotificationChannelCompat incomingChannel = new NotificationChannelCompat.Builder(
                INCOMING_CHANNEL_ID,
                NotificationManagerCompat.IMPORTANCE_HIGH
        )
                .setName("Incoming Calls")
                .setDescription("Handles the notifications when receiving a call")
                .setVibrationEnabled(true)
                .setSound(getRingtoneUri(), new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setLegacyStreamType(AudioManager.STREAM_RING)
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                        .build())
                .build();

        // Register notification channel
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.createNotificationChannel(incomingChannel);


        // Create content intent
        Intent intent = new Intent(context, IncommingCall.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Create decline intent
        Intent declineIntent = new Intent(context, DeclineCallService.class);
        declineIntent.putExtra("state", CallConstants.REJECT_CALL);
        PendingIntent declinePendingIntent = PendingIntent.getService(
                context,
                0,
                declineIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Create answer intent
        Intent answerIntent = new Intent(context, AnswerCallService.class);
        answerIntent.putExtra("state", CallConstants.ANSWER_CALL);
        PendingIntent answerPendingIntent = PendingIntent.getService(
                context,
                0,
                answerIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        //Create fullscreen intent
        Intent fullScreenIntent = new Intent(context, IncommingCall.class);
        PendingIntent pendingFullScreenIntent = PendingIntent.
                getActivity(context, 0, fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);


        Intent serviceIntent = new Intent(context, CallNotificationService.class);
        String name = TextUtils.isEmpty(call.getDetails().getCallerDisplayName()) ? call.getDetails().getCallerDisplayName(): "Unknown";
        serviceIntent.putExtra("callerName", name);
        serviceIntent.putExtra("declinePendingIntent", declinePendingIntent);
        serviceIntent.putExtra("answerPendingIntent", answerPendingIntent);
        serviceIntent.putExtra("contentIntent", contentIntent);
        serviceIntent.putExtra("pendingFullScreenIntent", pendingFullScreenIntent);
        context.startForegroundService(serviceIntent);

    }

    private static Uri getRingtoneUri() {
        return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
    }

    public static void showOutgoingCallNotification(Call call, Context context) {



        // Create decline intent
        Intent declineIntent = new Intent(context, DeclineCallService.class);
        declineIntent.putExtra("state", CallConstants.REJECT_CALL);
        PendingIntent declinePendingIntent = PendingIntent.getService(
                context,
                0,
                declineIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        // Create content intent
        Intent intent = new Intent(context, Dialer.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );



        Intent serviceIntent = new Intent(context, CallNotificationService.class);
        serviceIntent.putExtra("callerName", "Caller");
        serviceIntent.putExtra("declinePendingIntent", declinePendingIntent);
        serviceIntent.putExtra("contentIntent", contentIntent);
        context.startForegroundService(serviceIntent);
    }
}


