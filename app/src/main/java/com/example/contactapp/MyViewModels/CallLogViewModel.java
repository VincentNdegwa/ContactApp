package com.example.contactapp.MyViewModels;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.contactapp.Data.CallDetails;

import java.util.ArrayList;
import java.util.List;

public class CallLogViewModel extends ViewModel{
    public LiveData<List<CallDetails>> getContactLogs(Context context, String phoneNumber){
        MutableLiveData<List<CallDetails>> callLogs = new MutableLiveData<>();

        new Thread(()->{
            List<CallDetails> callEntries = getCallLogsFromProvide(context, phoneNumber);
            callLogs.postValue(callEntries);
        }).start();

        return callLogs;
    }
    @SuppressLint("Range")
    private List<CallDetails>  getCallLogsFromProvide(Context context, String phoneNumber) {
        List<CallDetails> callDetailsArray = new ArrayList<CallDetails>();

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

            Cursor cursor = context.getContentResolver().query(
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
                                context,
                                timeInMillis,
                                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME
                        );

                        String callType = getCallType(type);

                        String simInfo = sim == 1 ? "SIM1" : "SIM2";
                        String key = !TextUtils.isEmpty(name) ? name : number;
                        String visualName = TextUtils.isEmpty(name) ? number : name;

                        CallDetails callDetails = new CallDetails(name,number,formattedTime,simInfo,callType,timeInMillis);
                        callDetailsArray.add(callDetails);
                    }
                } finally {
                    cursor.close();
                }
            } else {
                Log.d("CallLogView", "Cursor is Null");
            }
        return callDetailsArray;
    };

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
