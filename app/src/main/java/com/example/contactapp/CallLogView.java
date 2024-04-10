package com.example.contactapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;

import com.example.contactapp.databinding.ActivityCallLogViewBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CallLogView extends AppCompatActivity {
    ActivityCallLogViewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String phoneNUmber = intent.getStringExtra("phoneNumber");
        Log.d("CallLogView", phoneNUmber);
        binding = ActivityCallLogViewBinding.inflate(getLayoutInflater());
        getData(phoneNUmber);
        setContentView(binding.getRoot());
    }
    @SuppressLint("Range")
    private void getData(String phoneNumber) {
        String selection = CallLog.Calls.NUMBER + "=?";
        String[] selectionArgs = {phoneNumber};

        String[] projection = {
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.NUMBER,
                CallLog.Calls.DATE,
                CallLog.Calls.PHONE_ACCOUNT_ID,
                CallLog.Calls.TYPE,
                CallLog.Calls.DURATION
        };

        Cursor cursor = getContentResolver().query(
                CallLog.Calls.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null
        );

        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
                    String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                    long timeInMillis = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));
                    int sim = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.PHONE_ACCOUNT_ID));
                    int type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));
                    long duration = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DURATION));

                    String formattedTime = DateUtils.formatDateTime(
                            this,
                            timeInMillis,
                            DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME
                    );

                    String callType = getCallType(type);

                    String simInfo = sim == 1 ? "SIM1" : "SIM2";
                    String key = !TextUtils.isEmpty(name) ? name : number;
                    String visualName = TextUtils.isEmpty(name) ? number : name;

                    Log.d("CallLogView", "Name: " + name);
                    Log.d("CallLogView", "Number: " + number);
                    Log.d("CallLogView", "Time: " + formattedTime);
                    Log.d("CallLogView", "Sim: " + simInfo);
                    Log.d("CallLogView", "Type: " + callType);
                    Log.d("CallLogView", "Duration: " + duration);
                    Log.d("CallLogView", "---------------------");
                }
            } finally {
                cursor.close();
            }
        } else {
            Log.d("CallLogView", "Cursor is Null");
        }
    }

    private String getCallType(int type) {
        switch (type) {
            case CallLog.Calls.INCOMING_TYPE:
                return "Incoming";
            case CallLog.Calls.OUTGOING_TYPE:
                return "Outgoing";
            case CallLog.Calls.MISSED_TYPE:
                return "Missed";
            default:
                return "Unknown";
        }
    }

}