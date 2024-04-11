package com.example.contactapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;

import com.example.contactapp.Adapters.ViewCallLogAdapter;
import com.example.contactapp.Data.CallDetails;
import com.example.contactapp.MyViewModels.CallLogViewModel;
import com.example.contactapp.databinding.ActivityCallLogViewBinding;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CallLogView extends AppCompatActivity {
    ActivityCallLogViewBinding binding;
    ViewCallLogAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String phoneNUmber = intent.getStringExtra("phoneNumber");
        binding = ActivityCallLogViewBinding.inflate(getLayoutInflater());
        getData(phoneNUmber);
        setContentView(binding.getRoot());
    }
    private void getData(String phoneNumber) {
        CallLogViewModel callLogViewModel = new CallLogViewModel();
        callLogViewModel.getContactLogs(this,phoneNumber).observe(this, callDetails -> {
                renderData(callDetails);
        });

    }

    private void renderData(List<CallDetails> callDetails) {
        adapter = new ViewCallLogAdapter(callDetails,this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        RecyclerView recyclerView = binding.callLogRecyclerview;
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);
    }
}