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

import com.example.contactapp.Data.CallDetails;
import com.example.contactapp.MyViewModels.CallLogViewModel;
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
    private void getData(String phoneNumber) {
        CallLogViewModel callLogViewModel = new CallLogViewModel();
        callLogViewModel.getContactLogs(this, phoneNumber).observe(this, CallDetailsList->{
            for (CallDetails log: CallDetailsList){
                Log.d("CallLogView", "Name: " + log.getName());
                Log.d("CallLogView", "Number: " + log.getNumber());
                Log.d("CallLogView", "--------------------------");
            }
        });
    }
}